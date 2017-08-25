package org.sage_one_sample.sageone;

	import java.io.File;
import java.io.FileInputStream;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.xml.XmlConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

	public class JettyRunner {

	    private static final Logger LOG = LoggerFactory.getLogger(JettyRunner.class);

	    private void start() {
	        try {
	        	
	            LOG.info("Starting Jetty Web Server ...");
	            final FileInputStream in = new FileInputStream(new File("src/test/resources/jetty.xml"));
	            final XmlConfiguration configuration = new XmlConfiguration(in);
				final  Server server = (Server) configuration.configure();
				final File webappDir = new File("src/main/webapp/");
				final WebAppContext context = new WebAppContext();
				context.setContextPath("/SageOneSampleApp");
				context.setResourceBase(webappDir.getAbsolutePath());

				// Important! make sure Jetty scans all classes under looking
				// for annotations. ie:  bin (eclipse), classes (idea), etc
				context.setAttribute("org.eclipse.jetty.server.webapp.ContainerIncludeJarPattern", ".*/bin/.*|.*/classes/.*|.*/target/.*");
				context.setParentLoaderPriority(true);

				// Config and launch server
				server.setHandler(context);
				server.start();	            
	 
	            LOG.info("Jetty Web Server on the fly ...");

	            server.join();

	        } catch (final Exception e) {
	            e.printStackTrace();
	            System.exit(100);
	        }
	    }

	    public static void main(final String[] args) {
	        final JettyRunner webServer = new JettyRunner();
	        webServer.start();
	    }

	}