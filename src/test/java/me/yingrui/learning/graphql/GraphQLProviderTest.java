package me.yingrui.learning.graphql;

import org.junit.Test;

import java.io.IOException;

public class GraphQLProviderTest {

    GraphQLDataFetchers graphQLDataFetchers = new GraphQLDataFetchers();

    @Test
    public void test() throws IOException {
        GraphQLProvider graphQLProvider = new GraphQLProvider(graphQLDataFetchers, null);
        graphQLProvider.initBySchema();
    }

}