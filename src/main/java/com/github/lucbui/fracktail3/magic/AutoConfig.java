package com.github.lucbui.fracktail3.magic;

import com.github.lucbui.fracktail3.magic.exception.BotConfigurationException;
import com.github.lucbui.fracktail3.magic.parse.xml.DefaultBotParser;
import com.github.lucbui.fracktail3.magic.resolver.SpringExpressionResolver;
import com.github.lucbui.fracktail3.xsd.DTDBot;
import com.github.lucbui.fracktail3.xsd.ObjectFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import java.net.MalformedURLException;
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
    public DTDBot dtdBot(@Value("${bot.xml.uri:}") String uri) throws JAXBException, SAXException, MalformedURLException {
        Unmarshaller jaxbUnmarshaller = JAXBContext.newInstance(ObjectFactory.class)
                .createUnmarshaller();
        jaxbUnmarshaller.setSchema(schema());

        if(StringUtils.isEmpty(uri)) {
            URL url = getClass().getResource("/xml/bot.xml");
            if (url == null) {
                throw new BotConfigurationException("No bot.xml found");
            }

            return ((JAXBElement<DTDBot>) jaxbUnmarshaller.unmarshal(url)).getValue();
        } else {
            return ((JAXBElement<DTDBot>) jaxbUnmarshaller.unmarshal(new URL(uri))).getValue();
        }
    }

    @Bean
    @ConditionalOnMissingBean
    public DefaultBotParser botParser(Environment environment) {
        DefaultBotParser parser = new DefaultBotParser();
        parser.setExpressionResolver(new SpringExpressionResolver(environment));
        return parser;
    }

    @Bean
    @ConditionalOnBean(DTDBot.class)
    public Bot bot(DefaultBotParser parser, DTDBot xmlBot) {
        return parser.fromXml(xmlBot);
    }
}
