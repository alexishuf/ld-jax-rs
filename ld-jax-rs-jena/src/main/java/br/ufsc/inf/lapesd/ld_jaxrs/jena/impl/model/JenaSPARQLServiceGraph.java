package br.ufsc.inf.lapesd.ld_jaxrs.jena.impl.model;

import br.ufsc.inf.lapesd.ld_jaxrs.model.*;
import org.apache.http.client.HttpClient;
import org.apache.http.protocol.HttpContext;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

import static br.ufsc.inf.lapesd.ld_jaxrs.model.SPARQLGraphHelper.*;

/**
 * Graph implementation that uses a (possibly remote) SPARQL endpoint as backend.
 *
 * This class can also be used with Traversers that do not use {@link Graph}'s methods, but
 * instead simply issue a SPARQL query, using createExecution().
 */
public class JenaSPARQLServiceGraph implements SPARQLGraph {
    private final @Nonnull String serviceURI;
    private final @Nonnull
    Function<String, QueryExecution> executionFactory;

    public JenaSPARQLServiceGraph(@Nonnull String serviceURI,
                                  @Nonnull Function<String, QueryExecution> executionFactory) {
        this.serviceURI = serviceURI;
        this.executionFactory = executionFactory;
    }

    public static JenaSPARQLServiceGraph fromService(String serviceURI, HttpClient httpClient,
                                                     HttpContext httpContext) {
        return new JenaSPARQLServiceGraph(serviceURI,
                q -> QueryExecutionFactory.sparqlService(serviceURI, q, httpClient, httpContext));
    }
    public static JenaSPARQLServiceGraph fromService(String serviceURI, HttpClient httpClient) {
        return new JenaSPARQLServiceGraph(serviceURI,
                q -> QueryExecutionFactory.sparqlService(serviceURI, q, httpClient));
    }

    public static JenaSPARQLServiceGraph fromService(String serviceURI) {
        return new JenaSPARQLServiceGraph(serviceURI,
                q -> QueryExecutionFactory.sparqlService(serviceURI, q));
    }

    @Nonnull
    @Override
    public String getServiceURI() {
        return serviceURI;
    }

    @Nonnull
    public QueryExecution createExecution(String query) {
        return executionFactory.apply(query);
    }

    @Nonnull
    @Override
    public Iterator<Triple> query(@Nullable Node subject, @Nullable Node predicate,
                                  @Nullable Node object) {
        List<Triple> list = new ArrayList<>();
        try (QueryExecution ex =
                     createExecution(SPARQLGraphHelper.selectBGP(subject, predicate, object))) {
            ResultSet results = ex.execSelect();
            while (results.hasNext()) {
                Node s = subject, p = predicate, o = object;
                QuerySolution row = results.next();
                if (row.contains(S)) s = new JenaNode(row.get(S));
                if (row.contains(P)) p = new JenaNode(row.get(P));
                if (row.contains(O)) o = new JenaNode(row.get(O));
                assert s != null;
                assert p != null;
                assert o != null;
                list.add(new SimpleTriple(s, p, o));
            }
        }
        return list.iterator();
    }

    @Nonnull
    @Override
    public Iterator<Node> querySubjects(PropertySpec... properties) {
        List<Node> list = new ArrayList<>();
        try (QueryExecution ex = createExecution(SPARQLGraphHelper.selectSubject(properties))) {
            ResultSet results = ex.execSelect();
            while (results.hasNext()) {
                list.add(new JenaNode(results.next().get(S)));
            }
        }
        return list.iterator();
    }

    @Nonnull
    @Override
    public Node getNode(@Nonnull String uri) {
        return new StringURINode(uri);
    }
}