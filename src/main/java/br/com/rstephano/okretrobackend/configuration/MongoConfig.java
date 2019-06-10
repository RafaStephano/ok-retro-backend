package br.com.rstephano.okretrobackend.configuration;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultMongoTypeMapper;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;

@Configuration
public class MongoConfig {

  @Autowired
  MongoDbFactory mongoDbFactory;
  @Autowired
  MongoMappingContext mongoMappingContext;

  @Bean
  public MappingMongoConverter mappingMongoConverter() {
    DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDbFactory);
    MappingMongoConverter converter = new MappingMongoConverter(dbRefResolver, mongoMappingContext);
    converter.setTypeMapper(new DefaultMongoTypeMapper(null));
    converter.setCustomConversions(customConversions());
    converter.afterPropertiesSet();
    return converter;
  }

  @Bean
  public MongoCustomConversions customConversions() {
    List<Converter<?, ?>> converters = new ArrayList<Converter<?, ?>>();
    converters.add(new DateToZonedDateTimeConverter());
    converters.add(new ZonedDateTimeToDateConverter());
    return new MongoCustomConversions(converters);
  }

  class DateToZonedDateTimeConverter implements Converter<Date, ZonedDateTime> {
    @Override
    public ZonedDateTime convert(Date source) {
      return source == null ? null : ZonedDateTime.ofInstant(source.toInstant(), ZoneOffset.UTC);
    }
  }

  class ZonedDateTimeToDateConverter implements Converter<ZonedDateTime, Date> {
    @Override
    public Date convert(ZonedDateTime source) {
      return source == null ? null : Date.from(source.toInstant());
    }
  }

}
