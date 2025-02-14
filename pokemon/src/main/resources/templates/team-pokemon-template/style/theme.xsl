<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- Colors -->
    <xsl:variable name="color-primary">#3498db</xsl:variable>
    <xsl:variable name="color-secondary">#2c3e50</xsl:variable>
    <xsl:variable name="color-background">#f8f9fa</xsl:variable>
    <xsl:variable name="color-text">#333333</xsl:variable>
    <xsl:variable name="color-muted">#666666</xsl:variable>
    <xsl:variable name="color-light">#ffffff</xsl:variable>
    <xsl:variable name="color-stat-bg">#edf2f7</xsl:variable>

    <!-- Typography -->
    <xsl:variable name="font-size-xl">32pt</xsl:variable>
    <xsl:variable name="font-size-lg">24pt</xsl:variable>
    <xsl:variable name="font-size-md">16pt</xsl:variable>
    <xsl:variable name="font-size-sm">14pt</xsl:variable>
    <xsl:variable name="font-size-xs">12pt</xsl:variable>
    <xsl:variable name="font-size-tiny">10pt</xsl:variable>

    <!-- Spacing -->
    <xsl:variable name="spacing-xl">1cm</xsl:variable>
    <xsl:variable name="spacing-lg">0.8cm</xsl:variable>
    <xsl:variable name="spacing-md">0.5cm</xsl:variable>
    <xsl:variable name="spacing-sm">0.3cm</xsl:variable>
    <xsl:variable name="spacing-xs">0.2cm</xsl:variable>

    <!-- Common Style Mixins -->
    <xsl:template name="header-text">
        <xsl:attribute name="font-size"><xsl:value-of select="$font-size-xl"/></xsl:attribute>
        <xsl:attribute name="font-weight">bold</xsl:attribute>
        <xsl:attribute name="text-align">center</xsl:attribute>
        <xsl:attribute name="color"><xsl:value-of select="$color-secondary"/></xsl:attribute>
    </xsl:template>

    <xsl:template name="card-container">
        <xsl:attribute name="margin-bottom"><xsl:value-of select="$spacing-lg"/></xsl:attribute>
        <xsl:attribute name="padding"><xsl:value-of select="$spacing-lg"/></xsl:attribute>
        <xsl:attribute name="border">2pt solid <xsl:value-of select="$color-primary"/></xsl:attribute>
        <xsl:attribute name="background-color"><xsl:value-of select="$color-light"/></xsl:attribute>
    </xsl:template>

    <xsl:template name="stat-block">
        <xsl:attribute name="font-size"><xsl:value-of select="$font-size-xs"/></xsl:attribute>
        <xsl:attribute name="background-color"><xsl:value-of select="$color-stat-bg"/></xsl:attribute>
        <xsl:attribute name="padding"><xsl:value-of select="$spacing-sm"/></xsl:attribute>
    </xsl:template>
</xsl:stylesheet>