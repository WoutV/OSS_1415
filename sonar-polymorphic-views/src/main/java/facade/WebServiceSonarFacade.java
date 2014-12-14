package facade;

import java.util.ArrayList;
import java.util.List;

import org.sonar.api.ServerExtension;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.services.DependencyQuery;
import org.sonar.wsclient.services.MetricQuery;
import org.sonar.wsclient.services.ResourceQuery;

import facade.ListUtils.Func;


/**
 * This class implements a facade for the Sonar database, by connecting to
 * the web service provided by the Sonar server.
 * 
 * @author Pieter Agten <pieter.agten@cs.kuleuven.be>
 *
 */
public class WebServiceSonarFacade implements SonarFacade, ServerExtension {
	public final static String DEFAULT_HOST = "http://localhost:9000";
	public final static String DEFAULT_USERNAME = "admin";
	public final static String DEFAULT_PASSWORD = "admin";
	private final Sonar sonar;
	
	/**
	 * Creates an instance of this class, using the default connection parameters.
	 * 
	 * A connection will be made to the Sonar web service, using the default hostname,
	 * username and password.
	 */
	public WebServiceSonarFacade() {
		this.sonar = Sonar.create(DEFAULT_HOST, DEFAULT_USERNAME, DEFAULT_PASSWORD);
	}
	
	/**
	 * Creates an instance of this class, using user-specified connection parameters.
	 * 
	 * @param host     The basic connection URL for the web service, e.g.: http://localhost:9000
	 * @param username The username to use for the web service connection 
	 * @param password The password to use for the web service connection
	 */
	public WebServiceSonarFacade(String host, String username, String password) {
		this.sonar = Sonar.create(host, username, password);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Resource findResource(String key) {
		org.sonar.wsclient.services.Resource queryResult = getSonar().find(new ResourceQuery(key));
		return new WSResource(queryResult);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Metric findMetric(String key) {
		org.sonar.wsclient.services.Metric queryResult = getSonar().find(MetricQuery.byKey(key));
		return queryResult == null ? null : new WSMetric(queryResult);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Resource> findProjects() {
		return doResourceQuery(new ResourceQuery().
				setQualifiers(org.sonar.wsclient.services.Resource.QUALIFIER_PROJECT));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Metric> findMetrics() {
		List<org.sonar.wsclient.services.Metric> queryResult = getSonar().findAll(MetricQuery.all());
		if (queryResult == null) return new ArrayList<Metric>();
		return ListUtils.map(queryResult, new Func<org.sonar.wsclient.services.Metric, Metric>() {
			public Metric execute(org.sonar.wsclient.services.Metric m) {
				return new WSMetric(m);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Resource> findPackages(Resource ancestor) {
		return doResourceQuery(new ResourceQuery(ancestor.getId()).
				setDepth(ResourceQuery.DEPTH_UNLIMITED).
				setQualifiers(org.sonar.wsclient.services.Resource.QUALIFIER_PACKAGE));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Resource> findClasses(Resource ancestor) {
		return doResourceQuery(new ResourceQuery(ancestor.getId()).
				setDepth(ResourceQuery.DEPTH_UNLIMITED).
				setQualifiers(org.sonar.wsclient.services.Resource.QUALIFIER_CLASS));
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Measure findMeasure(Resource resource, Metric metric) {
		List<Measure> measures = findMeasures(resource, metric);
		return measures.isEmpty() ? null : measures.get(0); 
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Measure> findMeasures(Resource resource, Metric ... metrics) {
		String[] metricKeys = new String[metrics.length];
		for (int i = 0; i < metrics.length; ++i) {
			metricKeys[i] = metrics[i].getKey();
		}
		final org.sonar.wsclient.services.Resource queryResult = getSonar().find(ResourceQuery.createForMetrics(resource.getKey(), metricKeys));
		if (queryResult == null) return new ArrayList<Measure>();
		return ListUtils.map(queryResult.getMeasures(), new Func<org.sonar.wsclient.services.Measure, Measure>() {
			public Measure execute(org.sonar.wsclient.services.Measure m) {
				return m == null ? null : new WSMeasure(m, queryResult.getKey());
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Measure> findMeasures(final Resource resource) {
		return ListUtils.map(getSonar().find(new ResourceQuery(resource.getKey())).getMeasures(), new Func<org.sonar.wsclient.services.Measure, Measure>() {
			public Measure execute(org.sonar.wsclient.services.Measure m) {
				return m == null ? null : new WSMeasure(m, resource.getKey());
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Dependency> findIncomingDependencies(Resource resource) {
		DependencyQuery query = DependencyQuery.createForIncomingDependencies(resource.getKey());
		return ListUtils.map(getSonar().findAll(query), new Func<org.sonar.wsclient.services.Dependency, Dependency>() {
			public Dependency execute(org.sonar.wsclient.services.Dependency d) {
				return d == null ? null : new WSDependency(d);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Dependency> findOutgoingDependencies(Resource resource) {
		DependencyQuery query = DependencyQuery.createForOutgoingDependencies(resource.getKey());
		return ListUtils.map(getSonar().findAll(query), new Func<org.sonar.wsclient.services.Dependency, Dependency>() {
			public Dependency execute(org.sonar.wsclient.services.Dependency d) {
				return d == null ? null : new WSDependency(d);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Dependency> findDependencies(Resource resource) {
		DependencyQuery query = DependencyQuery.createForResource(resource.getKey());
		return ListUtils.map(getSonar().findAll(query), new Func<org.sonar.wsclient.services.Dependency, Dependency>() {
			public Dependency execute(org.sonar.wsclient.services.Dependency d) {
				return d == null ? null : new WSDependency(d);
			}
		});
	}
	
	protected List<Resource> doResourceQuery(ResourceQuery query) {
		List<org.sonar.wsclient.services.Resource> queryResult = getSonar().findAll(query);
		if (queryResult == null) return new ArrayList<Resource>();
		return ListUtils.map(queryResult, new Func<org.sonar.wsclient.services.Resource, Resource>() {
			public Resource execute(org.sonar.wsclient.services.Resource r) {
				return r == null ? null : new WSResource(r);
			}
		});
	}
	
	protected Sonar getSonar() {
		return this.sonar;
	}
	
}
