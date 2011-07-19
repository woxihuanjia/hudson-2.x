/*******************************************************************************
 *
 * Copyright (c) 2004-2009 Oracle Corporation.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors: 
*
*    Kohsuke Kawaguchi
 *     
 *
 *******************************************************************************/ 

package hudson.model;

import hudson.ExtensionPoint;
import hudson.ExtensionList;
import hudson.Extension;
import hudson.ExtensionPoint.LegacyInstancesAreScopedToHudson;
import hudson.triggers.SCMTrigger;
import hudson.triggers.TimerTrigger;

import java.util.Set;
import java.io.IOException;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * Checks the health of a subsystem of Hudson and if there's something
 * that requires administrator's attention, notify the administrator.
 *
 * <h2>How to implement?</h2>
 * <p>
 * Plugins who wish to contribute such notifications can implement this
 * class and put {@link Extension} on it to register it to Hudson.
 *
 * <p>
 * Once installed, it's the implementor's responsibility to perform
 * monitoring and activate/deactivate the monitor accordingly. Sometimes
 * this can be done by updating a flag from code (see {@link SCMTrigger}
 * for one such example), while other times it's more convenient to do
 * so by running some code periodically (for this, use {@link TimerTrigger#timer})
 *
 * <p>
 * {@link AdministrativeMonitor}s are bound to URL by {@link Hudson#getAdministrativeMonitor(String)}.
 * See {@link #getUrl()}.
 *
 * <h3>Views</h3>
 * <dl>
 * <dt>message.jelly</dt>
 * <dd>
 * If {@link #isActivated()} returns true, Hudson will use the <tt>message.jelly</tt>
 * view of this object to render the warning text. This happens in the
 * <tt>http://SERVER/hudson/manage</tt> page. This view should typically render
 * a DIV box with class='error' or class='warning' with a human-readable text
 * inside it. It often also contains a link to a page that provides more details
 * about the problem.
 * </dd>
 * </dl>
 *
 * @author Kohsuke Kawaguchi
 * @since 1.273
 * @see Hudson#administrativeMonitors
 */
@LegacyInstancesAreScopedToHudson
public abstract class AdministrativeMonitor extends AbstractModelObject implements ExtensionPoint {
    /**
     * Human-readable ID of this monitor, which needs to be unique within the system.
     *
     * <p>
     * This ID is used to remember persisted setting for this monitor,
     * so the ID should remain consistent beyond the Hudson JVM lifespan.
     */
    public final String id;

    protected AdministrativeMonitor(String id) {
        this.id = id;
    }

    protected AdministrativeMonitor() {
        this.id = this.getClass().getName();
    }

    /**
     * Returns the URL of this monitor, relative to the context path, like "administrativeMonitor/foobar".
     */
    public String getUrl() {
        return "administrativeMonitor/"+id;
    }

    public String getDisplayName() {
        return id;
    }

    public final String getSearchUrl() {
        return getUrl();
    }

    /**
     * Mark this monitor as disabled, to prevent this from showing up in the UI.
     */
    public void disable(boolean value) throws IOException {
        Hudson hudson = Hudson.getInstance();
        Set<String> set = hudson.disabledAdministrativeMonitors;
        if(value)   set.add(id);
        else        set.remove(id);
        hudson.save();
    }

    /**
     * Returns true if this monitor {@link #disable(boolean) isn't disabled} earlier.
     *
     * <p>
     * This flag implements the ability for the admin to say "no thank you" to the monitor that
     * he wants to ignore.
     */
    public boolean isEnabled() {
        return !Hudson.getInstance().disabledAdministrativeMonitors.contains(id);
    }

    /**
     * Returns true if this monitor is activated and
     * wants to produce a warning message.
     *
     * <p>
     * This method is called from the HTML rendering thread,
     * so it should run efficiently.
     */
    public abstract boolean isActivated();

    /**
     * URL binding to disable this monitor.
     */
    public void doDisable(StaplerRequest req, StaplerResponse rsp) throws IOException {
        Hudson.getInstance().checkPermission(Hudson.ADMINISTER);
        disable(true);
        rsp.sendRedirect2(req.getContextPath()+"/manage");
    }

    /**
     * All registered {@link AdministrativeMonitor} instances.
     */
    public static ExtensionList<AdministrativeMonitor> all() {
        return Hudson.getInstance().getExtensionList(AdministrativeMonitor.class);
    }
}
