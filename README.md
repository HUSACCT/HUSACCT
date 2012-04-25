# HUSACCT project
_(This README is not done.)_

What does HUSACCT Stand for?

It stands for: Software Architecture Comformance Checking Tool  
HU stands for: Hogeschool Utrecht

## What is a Software Architecture Comformance Checking Tool?

When buiding new software, software architects design an architecture for the application. The programmers are tasked with realizing the application based on the designed architecture.

To see if the realized architecture is really the designed architecture we can use a SACC-Tool. The following steps are taken.

### Analyse an architecture

By analysing the application we can breakdown the structure of the application. That way we can use the physical entities that are found to define an architecture.  
It also allows to give architects and programmers a graphical representation of the realized software architecture.

### Define an architecture

Within this tool we define the architecture designed by the architects and the rules that apply to it. We create logical modules such as Layers and define rules that should be followed. Think of communication rules present in the MVC pattern.

1. `Logical Layer 'Presentation' is allowed to use Logical Layer 'Task', but not with Logical Layer 'Infrastructure'.`  
2. `Package 'database' is not allowed to use package 'gui'`.

### Mapping the defined architecture

Then we map the physical entities we found by analysing to the defined logical modules. That way the tool knows which physical entities (such as namespaces, packages, classes, interfaces, etc.) are present in which logical module.

1. `Package 'gui' should be present in Logical Layer 'Presentation'.`
2. `Package 'database.mysql' should be present in Logical Layer 'Infrastructure'.`

### Executing a conformance check

By executing a conformance check we can see if any of the specified rules are broken. A SACC-tool automates the process of checking wheter or not these rules are broken. The reports from this comformance check will list the violations that occur between entities.

Using the results from this conformance check programmers and architects can evaluate their realized software architecture. If there are serious issues they can improve it by changing the program so that it follows the specified rules. Making it the software it was meant to be.

## Why build a Software Architecture Comformance Checking Tool?

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

## Conclusion

The project is ongoing and is meant to be completed around July 2012.