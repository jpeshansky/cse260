/*
 * Copyright © 2015 Stefan Niederhauser (nidin@gmx.ch)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package guru.nidi.graphviz.engine;

import com.kitfox.svg.*;
import org.apache.batik.transcoder.*;
import org.apache.batik.transcoder.image.ImageTranscoder;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.util.function.Consumer;

import static java.awt.RenderingHints.*;

public enum Rasterizer {
    SALAMANDER {
        @Override
        public BufferedImage render(Graphviz graphviz, Consumer<Graphics2D> graphicsConfigurer, String svg) {
            final SVGDiagram diagram = createDiagram(svg);
            double scaleX = graphviz.scale;
            double scaleY = graphviz.scale;
            if (graphviz.width != 0 || graphviz.height != 0) {
                scaleX = graphviz.scale * graphviz.width / diagram.getWidth();
                scaleY = graphviz.scale * graphviz.height / diagram.getHeight();
                if (scaleX == 0) {
                    scaleX = scaleY;
                }
                if (scaleY == 0) {
                    scaleY = scaleX;
                }
            }
            final int width = (int) Math.ceil(scaleX * diagram.getWidth());
            final int height = (int) Math.ceil(scaleY * diagram.getHeight());
            final BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
            final Graphics2D graphics = image.createGraphics();
            configGraphics(graphics);
            if (graphicsConfigurer != null) {
                graphicsConfigurer.accept(graphics);
            }
            graphics.scale(scaleX, scaleY);
            renderDiagram(diagram, graphics);
            return image;
        }

        private SVGDiagram createDiagram(String svg) {
            final SVGUniverse universe = new SVGUniverse();
            final URI uri = universe.loadSVG(new StringReader(svg), "//graph/");
            final SVGDiagram diagram = universe.getDiagram(uri);
            diagram.setIgnoringClipHeuristic(true);
            return diagram;
        }

        private void renderDiagram(SVGDiagram diagram, Graphics2D graphics) {
            try {
                diagram.render(graphics);
            } catch (SVGException e) {
                throw new GraphvizException("Problem rendering SVG", e);
            }
        }

        private void configGraphics(Graphics2D graphics) {
            graphics.setRenderingHint(KEY_ALPHA_INTERPOLATION, VALUE_ALPHA_INTERPOLATION_QUALITY);
            graphics.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
            graphics.setRenderingHint(KEY_COLOR_RENDERING, VALUE_COLOR_RENDER_QUALITY);
            graphics.setRenderingHint(KEY_INTERPOLATION, VALUE_INTERPOLATION_BICUBIC);
            graphics.setRenderingHint(KEY_RENDERING, VALUE_RENDER_QUALITY);
            graphics.setRenderingHint(KEY_TEXT_ANTIALIASING, VALUE_TEXT_ANTIALIAS_ON);
        }
    },
    BATIK {
        @Override
        public BufferedImage render(Graphviz graphviz, Consumer<Graphics2D> graphicsConfigurer, String svg) {
            final BufferedImage[] image = new BufferedImage[1];
            final TranscoderInput in = new TranscoderInput(new StringReader(svg));
            try {
                final TranscoderOutput out = new TranscoderOutput(new OutputStream() {
                    @Override
                    public void write(int b) throws IOException {
                    }
                });
                final PNGTranscoder t = new PNGTranscoder() {
                    @Override
                    public BufferedImage createImage(int width, int height) {
                        return image[0] = super.createImage(width, height);
                    }
                };
                final TranscodingHints hints = new TranscodingHints(t.getTranscodingHints());
                if (graphviz.width != 0) {
                    hints.put(ImageTranscoder.KEY_WIDTH, (float) graphviz.scale * graphviz.width);
                }
                if (graphviz.height != 0) {
                    hints.put(ImageTranscoder.KEY_HEIGHT, (float) graphviz.scale * graphviz.height);
                }
                t.setTranscodingHints(hints);

                t.transcode(in, out);
                return image[0];
            } catch (TranscoderException e) {
                throw new GraphvizException("Error during rasterization", e);
            }
        }
    };

    public abstract BufferedImage render(Graphviz graphviz, Consumer<Graphics2D> graphicsConfigurer, String svg);
}
