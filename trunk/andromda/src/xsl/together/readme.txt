To: Gustavo Daniel Muzzillo
Cc: Developers AndroMDA; Users AndroMDA
Subject: RE: [Andromda-user] RE: [Uml2ejb-devel] about together
controlcenter 6 xmi outputs


The Together XMI output type 'XMI 1.1 for UML 1.4 (OMG)' appears to have a few errors in it.  

I detected what I belive to be 3 errors in the output.  Two of the errors are serious XML errors that prevent the file from being correctly processed by an XSL transformer.  The third error is easily corrected using XSL.

1) First line of file reads:

  <?xml version = '1.0' encoding = 'UTF8' ?>

The above line prevents an XSL processor from reading the file.  It should read:

   <?xml version = '1.0' encoding = 'UTF-8' ?>


2) Second line of file reads:

  <XMI xmi.version = '1.1' xmlns:UML = '//org.omg/UML/1.3'>

I believe that this is not valid namespace syntax given that it causes the XSL processor to complain. The above line should read:

  <XMI xmi.version = '1.1' xmlns:UML = 'org.omg/UML/1.3'>

*NOTE: I actually believe they are outputting UML 1.4 not UML 1.3.

3) All instances of:

<UML:StructuralFeature.type>
   <UML:Classifier>
     <UML:Namespace.ownedElement>
       <UML:DataType xmi.idref = 'G.4'/>
     </UML:Namespace.ownedElement>
   </UML:Classifier>
</UML:StructuralFeature.type>

should be converted to:

<UML:StructuralFeature.type>
  <UML:DataType xmi.idref = 'G.4'/>
</UML:StructuralFeature.type>

