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
package guru.nidi.graphviz.model;

import guru.nidi.graphviz.attribute.*;

import java.util.*;

import static java.util.stream.Collectors.joining;

public class MutableNode implements Linkable, MutableAttributed<MutableNode>, LinkTarget,
        MutableLinkSource<MutableNode> {
    protected Label label;
    protected final List<Link> links;
    protected final MutableAttributed<MutableNode> attributes;

    MutableNode() {
        this(null, new ArrayList<>(), Attributes.attrs());
    }

    protected MutableNode(Label label, List<Link> links, Attributes attributes) {
        this.label = label;
        this.links = links;
        this.attributes = new SimpleMutableAttributed<>(this, attributes);
        CreationContext.current().ifPresent(ctx -> ctx.nodes().applyTo(attributes));
    }

    public MutableNode copy() {
        return new MutableNode(label, new ArrayList<>(links), attributes.applyTo(Attributes.attrs()));
    }

    public MutableNode setLabel(Label label) {
        this.label = label;
        return this;
    }

    public MutableNode setLabel(String name) {
        return setLabel(Label.of(name));
    }

    public MutableNode merge(MutableNode n) {
        links.addAll(n.links);
        attributes.add(n.attributes);
        return this;
    }

    public MutableNodePoint withRecord(String record) {
        return new MutableNodePoint().setNode(this).setRecord(record);
    }

    public MutableNodePoint withCompass(Compass compass) {
        return new MutableNodePoint().setNode(this).setCompass(compass);
    }

    public MutableNode addLink(LinkTarget target) {
        final Link link = target.linkTo();
        links.add(Link.between(from(link), link.to).with(link.attributes));
        return this;
    }

    public MutableNode addLink(LinkTarget... targets) {
        for (final LinkTarget target : targets) {
            addLink(target);
        }
        return this;
    }

    public MutableNode addLink(String node) {
        return addLink(new MutableNode().setLabel(node));
    }

    public MutableNode addLink(String... nodes) {
        for (final String node : nodes) {
            addLink(node);
        }
        return this;
    }

    public MutableNode add(Attributes attrs) {
        attributes.add(attrs);
        return this;
    }

    @Override
    public Iterator<Map.Entry<String, Object>> iterator() {
        return attributes.iterator();
    }

    @Override
    public Attributes applyTo(MapAttributes attrs) {
        return attributes.applyTo(attrs);
    }

    private MutableNodePoint from(Link link) {
        if (link.from instanceof MutableNodePoint) {
            final MutableNodePoint f = (MutableNodePoint) link.from;
            return new MutableNodePoint().setNode(this).setRecord(f.record()).setCompass(f.compass());
        }
        return new MutableNodePoint().setNode(this);
    }

    public Label label() {
        return label;
    }

    @Override
    public Collection<Link> links() {
        return links;
    }

    @Override
    public Link linkTo() {
        return Link.to(this);
    }

    public MutableAttributed<MutableNode> attrs() {
        return attributes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final MutableNode node = (MutableNode) o;

        if (label != null ? !label.equals(node.label) : node.label != null) {
            return false;
        }
        if (links != null ? !links.equals(node.links) : node.links != null) {
            return false;
        }
        return !(attributes != null ? !attributes.equals(node.attributes) : node.attributes != null);

    }

    @Override
    public int hashCode() {
        int result = label != null ? label.hashCode() : 0;
        result = 31 * result + (links != null ? links.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return label + attributes.toString() + "->"
                + links.stream().map(l -> l.to.toString()).collect(joining(","));
    }
}
