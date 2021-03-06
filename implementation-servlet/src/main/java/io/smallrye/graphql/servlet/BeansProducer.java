/*
 * Copyright 2020 Red Hat, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.smallrye.graphql.servlet;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.graphql.ConfigKey;
import org.jboss.logging.Logger;

import graphql.schema.GraphQLSchema;
import io.smallrye.graphql.bootstrap.SmallRyeGraphQLBootstrap;
import io.smallrye.graphql.execution.ExecutionService;
import io.smallrye.graphql.execution.GraphQLConfig;

/**
 * Produce the services we want
 * 
 * @author Phillip Kruger (phillip.kruger@redhat.com)
 */
@ApplicationScoped
public class BeansProducer {
    private static final Logger LOG = Logger.getLogger(BeansProducer.class.getName());

    @Inject
    @ConfigProperty(name = ConfigKey.EXCEPTION_BLACK_LIST, defaultValue = "[]")
    private List<String> blackList;

    @Inject
    @ConfigProperty(name = ConfigKey.EXCEPTION_WHITE_LIST, defaultValue = "[]")
    private List<String> whiteList;

    @Inject
    @ConfigProperty(name = ConfigKey.DEFAULT_ERROR_MESSAGE, defaultValue = "Server Error")
    private String defaultErrorMessage;

    @Inject
    @ConfigProperty(name = "mp.graphql.printDataFetcherException", defaultValue = "false")
    private boolean printDataFetcherException;

    private GraphQLConfig config;

    @PostConstruct
    void init() {
        this.config = new GraphQLConfig();
        config.setBlackList(blackList);
        config.setWhiteList(whiteList);
        config.setPrintDataFetcherException(printDataFetcherException);
        config.setDefaultErrorMessage(defaultErrorMessage);

    }

    @Produces
    ExecutionService produceExecutionService() {
        return new ExecutionService(graphQLSchema, config);
    }

    @Produces
    GraphQLSchema produceGraphQLSchema() {
        return graphQLSchema;
    }

    static {
        SmallRyeGraphQLBootstrap bootstrap = new SmallRyeGraphQLBootstrap();
        graphQLSchema = bootstrap.bootstrap();
        LOG.info("SmallRye GraphQL Bootstrapped");
    }

    private static final GraphQLSchema graphQLSchema;
}
