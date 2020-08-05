package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.ObjectFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnResource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.URL;

@Configuration
public class AutoConfig {
    @Bean
    @ConditionalOnMissingBean
    public Schema schema() throws SAXException {
        SchemaFactory sf = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        return sf.newSchema(getClass().getResource("/xsd/schema.xsd"));
    }

    @Bean
    @SuppressWarnings("unchecked")
    @ConditionalOnMissingBean
    public DTDBot dtdBot() throws JAXBException, SAXException {
        Unmarshaller jaxbUnmarshaller = JAXBContext.newInstance(ObjectFactory.class)
                .createUnmarshaller();
        jaxbUnmarshaller.setSchema(schema());

        URL url = getClass().getResource("/xml/bot.xml");
        if(url == null) {
            throw new IllegalArgumentException("No bot.xml found");
        }

        return ((JAXBElement<DTDBot>)jaxbUnmarshaller.unmarshal(url)).getValue();
    }

    @Bean
    @ConditionalOnBean(DTDBot.class)
    public Bot bot(BotParser parser, DTDBot xmlBot) {
        return parser.fromXml(xmlBot);
    }
}