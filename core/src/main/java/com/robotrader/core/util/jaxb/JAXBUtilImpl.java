package com.robotrader.core.util.jaxb;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import org.apache.log4j.Logger;
import org.dom4j.io.XMLWriter;

/**
 *
 * @author anpilov
 */
public class JAXBUtilImpl implements JAXBUtil {

    private static Map contextTable = new HashMap();
    private static Logger log = Logger.getLogger(JAXBUtilImpl.class);

    private JAXBContext getJAXBContext(Class targetClass) throws Exception {
        if (targetClass == null) {
            log.debug("getJAXBContext() for class null. Return null");
            return null;
        }

        String className = targetClass.getName();
        log.debug("getJAXBContext() for class " + className);

        if (contextTable.get(className) != null) {
            log.debug("getJAXBContext() for class " + className + " found in cache");
            return (JAXBContext) contextTable.get(className);
        } else {
            log.debug("getJAXBContext() for class " + className + " not found in cache");
            return cacheJAXBContext(targetClass);
        }
    }

    @Override
    public Object unmarshallObject(Source source, Class targetClass) throws Exception {
        JAXBContext context = getJAXBContext(targetClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object result = unmarshaller.unmarshal(source);

        return result;
    }

    @Override
    public Object unmarshallObject(String source, Class targetClass) throws Exception {
        ByteArrayInputStream bais = new ByteArrayInputStream(source.getBytes());

        JAXBContext context = getJAXBContext(targetClass);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        Object result = unmarshaller.unmarshal(bais);

        return result;
    }

    @Override
    public String marshallObject(Object object) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        javax.xml.bind.JAXBContext jaxbCtx = getJAXBContext(object.getClass());
        javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        marshaller.marshal(object, baos);

        String sourceString = baos.toString("utf8");
//        StreamSource source = new StreamSource(new StringReader(sourceString));
        return sourceString;
    }

    @Override
    public Source marshallObject(Object object, String namespace) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        javax.xml.bind.JAXBContext jaxbCtx = getJAXBContext(object.getClass());
        javax.xml.bind.Marshaller marshaller = jaxbCtx.createMarshaller();
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_ENCODING, "UTF-8"); //NOI18N
        marshaller.setProperty(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

        NamespaceFilter filter = new NamespaceFilter(namespace, true);
        XMLWriter writer = new XMLWriter(baos);
        filter.setContentHandler(writer);

        marshaller.marshal(object, filter);
        String sourceString = baos.toString("utf8");

        StreamSource source = new StreamSource(new StringReader(sourceString));
        return source;
    }

    private static JAXBContext cacheJAXBContext(Class targetClass) throws Exception {
        JAXBContext targetContext = JAXBContext.newInstance(targetClass);

        String className = targetClass.getName();
        log.debug("cacheJAXBContext(). Save to cache JAXBContext for class " + className);
        contextTable.put(className, targetContext);

        return targetContext;
    }
}
