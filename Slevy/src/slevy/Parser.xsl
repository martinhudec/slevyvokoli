<?xml version="1.0" encoding="UTF-8"?>

<!--
    Document   : Parser.xsl
    Created on : Štvrtok, 2014, jún 19, 16:55
    Author     : martin
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml"/>

    <xsl:template match="/">
        <xsl:apply-templates select="//zlavy/zlava"/>
    </xsl:template>
    
    <xsl:template match="//zlavy/zlava">
        <zlava>
            <nazov>
                <xsl:apply-templates select="meno"/>
            </nazov>
            <adresa>
                <xsl:apply-templates select="adresa"/>
            </adresa>
        </zlava>
    </xsl:template>  
</xsl:stylesheet>
