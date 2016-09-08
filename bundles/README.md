Apache jclouds-karaf bundles
============================

This module provides convenience bundles to let jclouds run properly in OSGi environments.
Some of the bundles contained here are not publicly available, and others are just repackaged
to make them work well with jclouds.

The following convenience bundles are provided:

* **jsch-agentproxy-jsch**: Fixes the `org.apache.servicemix.bundles.jsch-agentproxy-jsch` bundle by not exporting the core packages that are already exported by other bundles. 
