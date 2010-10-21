/*
 * utils - AbstractService.java - Copyright © 2006-2009 David Roden
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package net.pterodactylus.util.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ThreadFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.pterodactylus.util.logging.Logging;
import net.pterodactylus.util.thread.DumpingThreadFactory;
import net.pterodactylus.util.validation.Validation;

/**
 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
 */
public abstract class AbstractService implements Service, Runnable {

	/** Logger. */
	private static final Logger logger = Logging.getLogger(AbstractService.class.getName());

	/** Listener support. */
	private final ServiceListenerManager serviceListenerSupport = new ServiceListenerManager(this);

	/** The shutdown hook. */
	private final ShutdownHook shutdownHook = new ShutdownHook();

	/** Counter for unnamed instances. */
	private static int counter = 0;

	/** Object used for synchronization. */
	protected final Object syncObject = new Object();

	/** Whether this method should stop. */
	private boolean shouldStop = false;

	/** The name of the service. */
	private final String name;

	/** The current state of the service. */
	private State state = State.offline;

	/** The current action of the service. */
	private String action = "";

	/** The thread factory to use. */
	private ThreadFactory threadFactory;

	/** The service attributes. */
	private final Map<String, Object> serviceAttributes = new HashMap<String, Object>();

	/** Whether to register the shutdown hook. */
	private final boolean registerShutdownHook;

	/**
	 * Constructs a new abstract service with an anonymous name.
	 */
	protected AbstractService() {
		this("AbstractService-" + counter++);
	}

	/**
	 * Constructs a new abstract service with the given name.
	 *
	 * @param name
	 *            The name of the service
	 */
	protected AbstractService(String name) {
		this(name, true);
	}

	/**
	 * Constructs a new abstract service with the given name.
	 *
	 * @param name
	 *            The name of the service
	 * @param threadFactory
	 *            The thread factory used to create the service thread
	 */
	protected AbstractService(String name, ThreadFactory threadFactory) {
		this(name, true, threadFactory);
	}

	/**
	 * Constructs a new abstract service with the given name.
	 *
	 * @param name
	 *            The name of the service
	 * @param registerShutdownHook
	 *            <code>true</code> to register shutdown hook for this service,
	 *            <code>false</code> to not register a shutdown hook
	 */
	protected AbstractService(String name, boolean registerShutdownHook) {
		this(name, registerShutdownHook, new DumpingThreadFactory(name + " ", false));
	}

	/**
	 * Constructs a new abstract service with the given name.
	 *
	 * @param name
	 *            The name of the service
	 * @param registerShutdownHook
	 *            <code>true</code> to register shutdown hook for this service,
	 *            <code>false</code> to not register a shutdown hook
	 * @param threadFactory
	 *            The thread factory used to create the service thread
	 */
	protected AbstractService(String name, boolean registerShutdownHook, ThreadFactory threadFactory) {
		this.registerShutdownHook = registerShutdownHook;
		Validation.begin().isNotNull("name", name).isNotNull("threadFactory", threadFactory).check();
		this.name = name;
		this.threadFactory = threadFactory;
	}

	//
	// EVENT MANAGEMENT
	//

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.service.Service#addServiceListener(net.pterodactylus.util.service.ServiceListener)
	 */
	@Override
	public void addServiceListener(ServiceListener serviceListener) {
		serviceListenerSupport.addListener(serviceListener);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.service.Service#removeServiceListener(net.pterodactylus.util.service.ServiceListener)
	 */
	@Override
	public void removeServiceListener(ServiceListener serviceListener) {
		serviceListenerSupport.removeListener(serviceListener);
	}

	//
	// ACCESSORS
	//

	/**
	 * Sets the thread factory that this service uses to spawn new threads.
	 *
	 * @param threadFactory
	 *            The thread factory for new threads
	 */
	public void setThreadFactory(ThreadFactory threadFactory) {
		Validation.begin().isNotNull("threadFactory", threadFactory).check();
		this.threadFactory = threadFactory;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.service.Service#getState()
	 */
	@Override
	public State getState() {
		synchronized (syncObject) {
			return state;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@Deprecated
	public String getStateReason() {
		synchronized (syncObject) {
			return action;
		}
	}

	/**
	 * Returns the current action of the service.
	 *
	 * @return The current action of the service
	 */
	@Override
	public String getAction() {
		synchronized (syncObject) {
			return action;
		}
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see net.pterodactylus.util.service.Service#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return name;
	}

	/**
	 * @see net.pterodactylus.util.service.Service#getServiceAttribute(java.lang.String)
	 */
	@Override
	public Object getServiceAttribute(String attributeName) {
		synchronized (syncObject) {
			return serviceAttributes.get(attributeName);
		}
	}

	/**
	 * @see net.pterodactylus.util.service.Service#hasServiceAttribute(java.lang.String)
	 */
	@Override
	public boolean hasServiceAttribute(String attributeName) {
		synchronized (syncObject) {
			return serviceAttributes.containsKey(attributeName);
		}
	}

	/**
	 * @see net.pterodactylus.util.service.Service#setServiceAttribute(java.lang.String,
	 *      java.lang.Object)
	 */
	@Override
	public void setServiceAttribute(String attributeName, Object attributeValue) {
		synchronized (syncObject) {
			serviceAttributes.put(attributeName, attributeValue);
		}
	}

	//
	// PROTECTED ACCESSORS
	//

	/**
	 * Sets the new state of this service without changing the action. Calling
	 * this method is equivalent to calling {@link #setState(State, String)
	 * setState(newState, null)}.
	 *
	 * @param newState
	 *            The new state of this service
	 * @see #setState(State, String)
	 */
	protected void setState(State newState) {
		setState(newState, null);
	}

	/**
	 * Sets the action for the current state.
	 *
	 * @param action
	 *            The new action of the service, or <code>null</code> to not
	 *            change the action
	 * @deprecated Use {@link #setAction(String)} instead
	 */
	@Deprecated
	protected void setStateReason(String action) {
		setAction(action);
	}

	/**
	 * Sets the action of the service.
	 *
	 * @param action
	 *            The new action of the service, or <code>null</code> to not
	 *            change the action
	 */
	protected void setAction(String action) {
		if (action != null) {
			synchronized (syncObject) {
				this.action = action;
			}
		}
	}

	/**
	 * Sets the new state of this service. If the current state is different
	 * from the given new state, a
	 * {@link ServiceListener#serviceStateChanged(Service, State, State)
	 * Service-State-Changed} event is fired. If the given action is
	 * <code>null</code>, the current action will not be changed.
	 *
	 * @param newState
	 *            The new state of the service
	 * @param action
	 *            The current action of the service, or <code>null</code> if the
	 *            action should not be changed
	 */
	protected void setState(State newState, String action) {
		State oldState = null;
		synchronized (syncObject) {
			oldState = state;
			state = newState;
			if (action != null) {
				this.action = action;
			}
		}
		if (oldState != newState) {
			serviceListenerSupport.fireServiceStateChanged(oldState, newState);
		}
	}

	/**
	 * Returns whether this service should stop.
	 *
	 * @return <code>true</code> if this service should stop, <code>false</code>
	 *         otherwise
	 */
	protected boolean shouldStop() {
		synchronized (syncObject) {
			return shouldStop;
		}
	}

	//
	// SERVICE METHODS
	//

	/**
	 * Empty initialization. If your service needs to initialize anything before
	 * being started, simply override this method.
	 *
	 * @see #init()
	 */
	protected void serviceInit() {
		/* do nothing. */
	}

	/**
	 * Initializes this services. This method returns immediately if the current
	 * {@link #state} of this service is <strong>not</strong>
	 * {@link State#offline}. Otherwise {@link #serviceInit()} is called for the
	 * real initialization of this service.
	 *
	 * @see #serviceInit()
	 * @see net.pterodactylus.util.service.Service#init()
	 */
	@Override
	public final void init() {
		if (state.getBasicState() != State.offline) {
			logger.log(Level.WARNING, "will not init " + name + ", state is " + getState());
			return;
		}
		serviceInit();
	}

	/**
	 * Empty implementation. If your service needs to do something before the
	 * service thread is started, simply override this method.
	 *
	 * @see #start()
	 */
	protected void serviceStart() {
		/* do nothing. */
	}

	/**
	 * Attempts to start this service. This method returns immediately if the
	 * current {@link #state} is <strong>not</strong> {@link State#offline}.
	 * Otherwise {@link #serviceStart()} is called for further post-init,
	 * pre-start initialization.
	 *
	 * @see #serviceStart()
	 * @see net.pterodactylus.util.service.Service#start()
	 */
	@Override
	public final void start() {
		if (getState() != State.offline) {
			logger.log(Level.WARNING, "will not start " + name + ", state is " + getState());
			return;
		}
		if (registerShutdownHook) {
			Runtime.getRuntime().addShutdownHook(shutdownHook);
		}
		serviceStart();
		synchronized (syncObject) {
			shouldStop = false;
		}
		setState(State.starting, "");
		Thread serviceThread = threadFactory.newThread(this);
		serviceThread.setName(name);
		serviceThread.start();
	}

	/**
	 * Empty implementation. If your service needs to do anything this method
	 * needs to be overridden. Otherwise it will {@link #sleep()} until
	 * {@link #stop()} is called.
	 *
	 * @see #run()
	 */
	protected void serviceRun() {
		while (!shouldStop()) {
			sleep(0);
		}
	}

	/**
	 * Runner for the main {@link #serviceRun()} method. This method sets the
	 * {@link #state} to {@link State#online} and fires a
	 * {@link ServiceListener#serviceStarted(Service) Service-Started} event
	 * before {@link #serviceRun()} is called. When the {@link #serviceRun()}
	 * method terminates the {@link #state} is set to {@link State#offline}
	 * without changing the action. If the {@link #serviceRun()} method threw an
	 * exception {@link #state} is set to {@link State#offline} with the
	 * {@link Exception#getMessage() message} of the exception as action. In
	 * both cases a {@link ServiceListener#serviceStopped(Service, Throwable)
	 * Service-Stopped} event is fired.
	 */
	@Override
	public final void run() {
		Throwable cause = null;
		try {
			setState(State.online);
			serviceListenerSupport.fireServiceStarted();
			serviceRun();
		} catch (Throwable t) {
			cause = t;
		} finally {
			setState(State.offline, (cause != null) ? cause.getMessage() : null);
			serviceListenerSupport.fireServiceStopped(cause);
		}
	}

	/**
	 * Empty implementation. If you need to perform actions on stopping, simply
	 * override this method.
	 *
	 * @see #stop()
	 */
	protected void serviceStop() {
		/* do nothing. */
	}

	/**
	 * Stops this service. This method returns immediately if the basic state of
	 * {@link #state} is not {@link State#online} or {@link State#starting}.
	 * Otherwise {@link #shouldStop} is set to <code>true</code>,
	 * {@link #serviceStop} is called, and sleeping threads (if any) are
	 * notified.
	 *
	 * @see net.pterodactylus.util.service.Service#stop()
	 */
	@Override
	public final void stop() {
		synchronized (syncObject) {
			shouldStop = true;
			syncObject.notify();
		}
		if (registerShutdownHook) {
			Runtime.getRuntime().removeShutdownHook(shutdownHook);
		}
		serviceStop();
	}

	/**
	 * Empty implementation. If you need to free resources used in a service,
	 * simply override this method.
	 *
	 * @see #destroy()
	 */
	protected void serviceDestroy() {
		/* do nothing. */
	}

	/**
	 * Destroys this service, freeing all used resources. This method will
	 * return immediately if {@link #state} is not {@link State#offline}.
	 * Otherwise {@link #serviceDestroy} is called and internal resources are
	 * freed.
	 *
	 * @see net.pterodactylus.util.service.Service#destroy()
	 */
	@Override
	public final void destroy() {
		serviceDestroy();
	}

	//
	// PROTECTED ACTIONS
	//

	/**
	 * Sleeps until {@link #syncObject} gets {@link #notify()}'ed.
	 */
	protected void sleep() {
		sleep(0);
	}

	/**
	 * Sleeps until {@link #syncObject} gets {@link #notify()}'ed or the
	 * specified timeout (in milliseconds) has elapsed. This method will return
	 * immediately if the service has already been told to {@link #stop()}.
	 *
	 * @param timeout
	 *            The number of milliseconds to wait
	 */
	protected void sleep(long timeout) {
		synchronized (syncObject) {
			if (!shouldStop) {
				try {
					syncObject.wait(timeout);
				} catch (InterruptedException ie1) {
					/* FIXME - ignore. */
				}
			}
		}
	}

	/**
	 * {@link Object #notify() Notifies} the {@link #syncObject} to interrupt a
	 * {@link #sleep()}.
	 */
	protected void notifySyncObject() {
		synchronized (syncObject) {
			syncObject.notify();
		}
	}

	/**
	 * Shutdown hook that is run when the VM is told to exit.
	 *
	 * @author <a href="mailto:bombe@pterodactylus.net">David ‘Bombe’ Roden</a>
	 */
	private class ShutdownHook extends Thread implements ServiceListener {

		/** Object used for synchronization. */
		@SuppressWarnings("hiding")
		private final Object syncObject = new Object();

		/** Whether the service has stopped. */
		private boolean stopped = true;

		/**
		 * Creates a new shutdown hook.
		 */
		public ShutdownHook() {
			super("Shutdown Hook for " + AbstractService.this);
			AbstractService.this.addServiceListener(this);
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		public void run() {
			logger.log(Level.INFO, "shutdown hook for " + AbstractService.this + " started.");
			synchronized (syncObject) {
				if (!stopped) {
					AbstractService.this.stop();
				}
				while (!stopped) {
					logger.log(Level.FINER, "waiting for " + AbstractService.this + " to stop...");
					try {
						syncObject.wait();
					} catch (InterruptedException ie1) {
						/* ignore, continue waiting. */
					}
				}
			}
			logger.log(Level.INFO, "shutdown hook for " + AbstractService.this + " finished.");
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		public void serviceStarted(Service service) {
			logger.log(Level.FINER, AbstractService.this + " started.");
			synchronized (syncObject) {
				stopped = false;
			}
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		public void serviceStateChanged(Service service, net.pterodactylus.util.service.State oldState, net.pterodactylus.util.service.State newState) {
			/* ignore. */
		}

		/**
		 * {@inheritDoc}
		 */
		@Override
		@SuppressWarnings("synthetic-access")
		public void serviceStopped(Service service, Throwable cause) {
			logger.log(Level.FINE, AbstractService.this + " stopped.", cause);
			synchronized (syncObject) {
				stopped = true;
				syncObject.notify();
			}
		}

	}

}
