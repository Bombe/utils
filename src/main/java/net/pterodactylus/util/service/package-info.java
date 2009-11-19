/**
 * Basic interface and implementation for services that can be started, stopped
 * and run asynchronously in the background. <h2>Usage of the Service Interface</h2>
 * <p>
 * Using a {@link net.pterodactylus.util.service.Service} is pretty straight-forward:
 *
 * <pre>
 *  Service someService = new SomeService();
 *  someService.init();
 *  someService.setParameter1(...);
 *  someService.setParameter2(...);
 *  // some more preparations
 *  // and finally...
 *  someService.start();
 * </pre>
 *
 * <h2>Implementing Own Services</h2>
 * <p>
 * If you want to implement your own service it is recommended that you override
 * the {@link net.pterodactylus.util.service.AbstractService} base class as it takes care
 * of a couple of things for you, like creating the thread that runs your
 * service, managing the {@link net.pterodactylus.util.service.Service#stop()} method,
 * providing synchronized access to state variables, and other things.
 * </p>
 * <p>
 * The {@link net.pterodactylus.util.service.AbstractService} base class adds a
 * <code>service</code> method for each of the
 * {@link net.pterodactylus.util.service.Service#init()},
 * {@link net.pterodactylus.util.service.Service#start()},
 * {@link net.pterodactylus.util.service.Service#stop()}, and
 * {@link net.pterodactylus.util.service.Service#destroy()} methods that let you take care
 * of things in your own service without having to interfere with the stuff at
 * {@link net.pterodactylus.util.service.AbstractService} does for you. The
 * {@link net.pterodactylus.util.service.AbstractService#serviceRun()} method is the place
 * to implement your magic then. It should be designed as an infinite loop like
 * this:
 *
 * <pre>
 * protected void serviceRun() {
 * 	while (!shouldStop()) {
 * 		doStuff();
 * 		doLengthyStuff();
 * 		if (shouldStop()) {
 * 			continue;
 * 		}
 * 		doMoreLengthyStuff();
 * 	}
 * }
 * </pre>
 *
 * Checking the {@link net.pterodactylus.util.service.AbstractService#shouldStop()} every
 * once in a while ensures that your service stops within a short time after
 * {@link net.pterodactylus.util.service.Service#stop()} has been called.
 *
 */
package net.pterodactylus.util.service;