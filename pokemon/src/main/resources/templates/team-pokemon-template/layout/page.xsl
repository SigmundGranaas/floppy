<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template name="create-page-layout">
        <fo:layout-master-set>
            <fo:simple-page-master master-name="team"
                                   page-height="29.7cm"
                                   page-width="21cm"
                                   margin="1.5cm">
                <fo:region-body margin-top="2cm" margin-bottom="2cm"/>
                <fo:region-before extent="2cm"/>
                <fo:region-after extent="2cm"/>
            </fo:simple-page-master>
        </fo:layout-master-set>
    </xsl:template>

    <xsl:template name="header">
        <fo:static-content flow-name="xsl-region-before">
            <fo:block font-size="10pt" text-align="right" color="#666666">
                <xsl:value-of select="teamName"/> - Page <fo:page-number/>
            </fo:block>
        </fo:static-content>
    </xsl:template>

    <xsl:template name="footer">
        <fo:static-content flow-name="xsl-region-after">
            <fo:block font-size="8pt" text-align="center" color="#666666">
                Formation Date: <xsl:value-of select="formationDate"/>
            </fo:block>
        </fo:static-content>
    </xsl:template>
</xsl:stylesheet>
