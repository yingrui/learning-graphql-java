package me.yingrui.learning.graphql;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import graphql.GraphQL;
import graphql.language.FieldDefinition;
import graphql.language.ObjectTypeDefinition;
import graphql.language.Type;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.leangen.graphql.GraphQLSchemaGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Component
public class GraphQLProvider {

    private GraphQLDataFetchers graphQLDataFetchers;
    private GraphQL graphQL;
    private BookRepository bookRepository;

    public GraphQLProvider(GraphQLDataFetchers graphQLDataFetchers, BookRepository bookRepository) {
        this.graphQLDataFetchers = graphQLDataFetchers;
        this.bookRepository = bookRepository;
    }

    @PostConstruct
    public void init() {
        initByCode();
    }

    private void initByCode() {
        Query query = new Query(bookRepository); //create or inject the service beans

        GraphQLSchema graphQLSchema = new GraphQLSchemaGenerator()
                .withOperationsFromSingletons(query) //register the beans
                .generate();
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    public void initBySchema() throws IOException {
        URL url = Resources.getResource("schema.graphqls");
        String sdl = Resources.toString(url, Charsets.UTF_8);
        GraphQLSchema graphQLSchema = buildSchema(sdl);
        this.graphQL = GraphQL.newGraphQL(graphQLSchema).build();
    }

    private GraphQLSchema buildSchema(String sdl) {
        TypeDefinitionRegistry typeRegistry = new SchemaParser().parse(sdl);
        RuntimeWiring runtimeWiring = buildWiring(typeRegistry);
        SchemaGenerator schemaGenerator = new SchemaGenerator();
        return schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring);
    }

    private RuntimeWiring buildWiring(TypeDefinitionRegistry typeRegistry) {
        RuntimeWiring.Builder builder = RuntimeWiring.newRuntimeWiring();

        List<ObjectTypeDefinition> types = typeRegistry.getTypes(ObjectTypeDefinition.class);
        for (ObjectTypeDefinition typeDefinition : types) {
            String currentObjectTypeName = typeDefinition.getName();
            System.out.println(currentObjectTypeName);
            List<FieldDefinition> fieldDefinitions = typeDefinition.getFieldDefinitions();
            for (FieldDefinition fieldDefinition : fieldDefinitions) {
                Type type = fieldDefinition.getType();
                if (typeRegistry.isObjectType(type) && fieldDefinition.getInputValueDefinitions().isEmpty()) {
                    System.out.println(fieldDefinition);

                }
            }

            System.out.println();
        }

        return builder
                .type(newTypeWiring("Query")
                        .dataFetcher("getBookById", graphQLDataFetchers.getBookByIdDataFetcher()))
                .type(newTypeWiring("Query")
                        .dataFetcher("books", graphQLDataFetchers.getDataSearcher()))
                .type(newTypeWiring("Book")
                        .dataFetcher("author", graphQLDataFetchers.getAuthorDataFetcher()))
                .build();
    }

    @Bean
    public GraphQL graphQL() {
        return graphQL;
    }

}