# HUSACCT project

HUSACCT means: HU Software Architecture Compliance Checking Tool, where HU stands for: <a href="http://international.hu.nl/" title="HU" target="_blank">HU University of Applied Sciences Utrecht</a>.
The HUSACCT project is conducted at the Institute for ICT, located in Utrecht, The Netherlands. Students of the specialization "Advanced Software Enhineering" have participated actively during the spring semesters of 2011-2013. 
During the Spring semester of 2011, four teams of students developed the first prototypes.
During the spring semesters of 2012 and 2013, six teams, with four to five students per team, worked concurrently on the tool. Each team was responsible for a different service: 1) Control; 2) Analyze Java; 3) Analyze .NET; 4) Define; 5) Validate; and 6) Graphics.
In December 2012 version 1.0 was released and in September 2013 version 2.0. 
Since then, the development of HUSACCT has been going on.

To download the build, watch an introduction video, or read user documentation, visit: http://husacct.github.io/HUSACCT/ 

## Functionality

HUSACCT provides support for software architecture compliance checking (SACC) of Java and C# systems. SACC monitors the conformance of the implemented architecture (in the source code) to the intended software architecture (in the system's design). 
The prominent feature of HUSACCT is the support of semantically rich modular architectures (SRMAs), which are composed of modules of different types (like software subsystems, layers and components) and rules of different types. To perform an SACC, an intended software architecture is defined first. Next, HUSACCT checks the compliance to these rules, based on static analysis of the source code, and it reports infringements. 

Apart from ACC-support, HUSACCT provides support for architecture reconstruction as well: the static structure of the software may be visualized, browsed, and reported.

HUSACCT itself is written in Java. 
Currently there is support for the following **languages**: Java and C#.

## Documentation

User and system documentation is available in the doc directory of the master branch.  

## Plugins

Note: The plugins are developed with v.1.0 in 2012 and not developed any further, afterwards.  

### Eclipse IDE

An [Eclipse](http://www.eclipse.org/) plugin is available. ([Eclipse plugin repository](https://github.com/HUSACCT/Eclipse-plugin))

### Maven

A [Maven](http://maven.apache.org/) plugin is also available. ([Maven plugin repository](https://github.com/HUSACCT/Maven-plugin))




