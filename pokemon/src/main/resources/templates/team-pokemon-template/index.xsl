<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- Import all required components -->
    <xsl:import href="layout/page.xsl"/>
    <xsl:import href="component/pokemon-card.xsl"/>
    <xsl:import href="component/team-header.xsl"/>
    <xsl:import href="style/theme.xsl" />

    <!-- Root template -->
    <xsl:template match="/">
        <fo:root>
            <xsl:call-template name="create-page-layout"/>
            <fo:page-sequence master-reference="team">
                <!-- Header with teamName -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block font-size="10pt" text-align="right" color="#666666">
                        <xsl:value-of select="/*/teamName"/> - Page <fo:page-number/>
                    </fo:block>
                </fo:static-content>

                <!-- Footer with formation date -->
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="8pt" text-align="center" color="#666666">
                        Formation Date: <xsl:value-of select="/*/formationDate"/>
                    </fo:block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body">
                    <!-- Team header with trainer info -->
                    <xsl:call-template name="team-header">
                        <xsl:with-param name="teamName" select="/*/teamName"/>
                        <xsl:with-param name="trainerName" select="/*/trainerName"/>
                    </xsl:call-template>

                    <!-- Process each team member -->
                    <xsl:apply-templates select="/*/team"/>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>