package com.alphasystem.docbook.builder.test;

import com.alphasystem.util.IdGenerator;
import com.alphasystem.util.JAXBTool;
import org.apache.commons.lang3.StringUtils;
import org.docbook.model.*;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.namespace.QName;

/**
 * @author sali
 */
public final class DataFactory {
    private static final JAXBTool jaxbTool = new JAXBTool();

    private static final ObjectFactory objectFactory = new ObjectFactory();

    static {
        jaxbTool.setMarshallerProperty("jaxb.formatted.output", false);
    }

    public static Article createArticle(Object... content) {
        return createArticle(null, content);
    }

    public static Article createArticle(String title, Object... content) {
        var article = new Article().withId(IdGenerator.nextId());
        if (StringUtils.isNotBlank(title)) {
            article.withContent(createInfo(title));
        }
        return article.withContent(content);
    }

    public static Info createInfo(Object... title) {
        return new Info().withContent(new Title().withContent(title));
    }

    public static Emphasis createBold(Object... content) {
        return createEmphasis("strong", content);
    }


    public static Emphasis createEmphasis(String role, Object... content) {
        return objectFactory.createEmphasis().withRole(role).withContent(content);
    }


    public static Literal createLiteral(String id, Object... content) {
        return objectFactory.createLiteral().withId(id).withContent(content);
    }


    public static Phrase createPhrase(String role, Object... content) {
        return objectFactory.createPhrase().withRole(role).withContent(content);
    }

    public static SimplePara createSimplePara(String id, Object... content) {
        return objectFactory.createSimplePara().withId(id).withContent(content);
    }

    public static Para createPara(String id, Object... content) {
        return objectFactory.createPara().withId(id).withContent(content);
    }

    public static FormalPara createFormalPara(String id,  String role,Title title, Para para) {
        return objectFactory.createFormalPara().withId(id).withRole(role).withTitleContent(title).withPara(para);
    }

    public static CrossReference createCrossReference(Object content) {
        return new CrossReference().withLinkend(content);
    }

    public static Link createLink(Object linkEnd, String endTerm, String href) {
        return new Link().withLinkend(linkEnd).withEndterm(endTerm).withHref(href);
    }

    public static Subscript createSubscript(String id, Object... content) {
        return objectFactory.createSubscript().withId(id).withContent(content);
    }

    public static Superscript createSuperscript(String id, Object... content) {
        return objectFactory.createSuperscript().withId(id).withContent(content);
    }

    public static Title createTitle(Object... content) {
        return objectFactory.createTitle().withContent(content);
    }


    @SuppressWarnings("unchecked")
    public static String toXml(Object obj) {
        final var clazz = obj.getClass();
        final var r = (XmlRootElement) clazz.getAnnotation(XmlRootElement.class);
        try {
            return jaxbTool.marshall(clazz.getPackageName(), new JAXBElement(new QName(r.namespace(), r.name()), clazz, obj));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
