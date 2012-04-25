# HUSACCT project
_(This README is not done.)_

What does HUSACCT Stand for?

It stands for: Software Architecture Comformance Checking Tool  
HU stands for: [Hogeschool Utrecht](http://international.hu.nl/) (HU University of Applied Sciences Utrecht, The Netherlands. The school at which this project is taken place.)

## What is a Software Architecture Conformance Checking Tool?

When building new software, software architects design an architecture for the application. The programmers are tasked with realizing the application based on the designed architecture.

To see if the realized architecture is really the designed architecture we can use a SACC-Tool. The following steps are taken to confirm this.

### Analyse an architecture

By analyzing the built application we can breakdown the structure of the application. That way we can use the physical entities that are detected to define an architecture.  
It also allows architects and programmers to view a graphical representation of the realized software architecture, but that is not good enough. We want to be able to check the analyzed software architecture against an architectural model.

### Define an architecture

Within this tool we define the architecture designed by the architects and the rules that apply to it. We create logical modules such as [Layers](http://en.wikipedia.org/wiki/Common_layers_in_an_information_system_logical_architecture) and define rules that should be followed. Think of the communication rules present in the [MVC pattern](http://en.wikipedia.org/wiki/Model-view-controller).

Examples:

1. `Logical Layer 'Presentation' is allowed to use Logical Layer 'Task',`  
   `but not with Logical Layer 'Infrastructure'.`  
2. `Package 'database' is not allowed to use package 'gui'`.  
2. `Class 'MySQLDatbaseImpl' is not allowed to use class 'UserRegistrationController'`.  

_(not actual syntax/code used in the tool)_  

### Mapping the defined architecture

We then map the physical entities we found, by analyzing, to the defined logical modules. That way the tool knows which physical entities (such as namespaces, packages, classes, interfaces, etc.) are present in which logical module.

Examples:

1. `Package 'gui' should be present in Logical Layer 'Presentation'.`
2. `Package 'database.mysql' should be present in Logical Layer 'Infrastructure'.`
2. `Class 'UserRegistrationController' should be present in Logical Layer 'Presentation'.`

_(not actual syntax/code used in the tool)_  

### Executing a conformance check

By executing a conformance check we can see if any of the specified rules are broken. A SACC-Tool automates the process of checking whether or not these rules are broken. The reports from this conformance check will list/graphically display the violations that occur between entities.

Using the results from this conformance check programmers and architects can evaluate their realized software architecture. If there are serious issues they can improve it by changing the program so that it follows the specified rules. That way they are making it the software it was meant to be.

## Why build a Software Architecture Conformance Checking Tool?

After conducting research into other SACC-tools we found out that the tools not always are as user friendly and accurate as we had expected. We were then assigned to create our own SACC-tool and improve on some concept we found lacking in others. 

The application we were instructed to build is meant to be a contribution to the field of Software Architecture analysis and evaluation.

## Support

The tool itself is written in Java.  
Currently there is (only) support for the following **languages**:

- Java _(Using Antler)_
- .NET C# _(Using Antler)_

We are trying to build support for the following **rule types**:

- Is allowed to use
- Is not allowed to use
- more?

The architecture should allow for expansions to support more languages and rule types.

### Eclipse IDE

We are also working on making this an [Eclipse](http://www.eclipse.org/) plugin.

## Build team

This project involves six teams from one class (Bachelor Computer Science Informatics 2011-2012); around 4 to 5 people a team. Each team is responsible for a different service (control, analyze (Java and .NET), define, validate and architecture graphics).  
The three teachers involved are.. _(Not willing to put names here yet.)_

## Conclusion

The project is ongoing and is meant to be completed around July 2012.