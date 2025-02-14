<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:import href="../style/theme.xsl"/>

    <xsl:template name="stat-cell">
        <xsl:param name="stat"/>
        <xsl:param name="name"/>
        <fo:table-cell padding="0.2cm">
            <fo:block font-size="12pt"
                      background-color="#edf2f7"
                      padding="0.3cm"
                      text-transform="capitalize">
                <xsl:value-of select="$name"/>: <xsl:value-of select="$stat"/>
            </fo:block>
        </fo:table-cell>
    </xsl:template>

    <xsl:template name="pokemon-stats">
        <fo:block font-size="14pt"
                  font-weight="bold"
                  color="{$color-primary}"
                  space-before="0.8cm"
                  space-after="0.3cm">
            Stats:
        </fo:block>
        <fo:table width="100%" table-layout="fixed">
            <fo:table-column column-width="proportional-column-width(1)"
                             number-columns-repeated="3"/>
            <fo:table-body>
                <xsl:for-each select="stats/*[position() mod 3 = 1]">
                    <xsl:variable name="current-node" select="."/>
                    <xsl:variable name="next-node" select="following-sibling::*[1]"/>
                    <xsl:variable name="next-next-node" select="following-sibling::*[2]"/>

                    <fo:table-row>
                        <!-- Current stat -->
                        <xsl:call-template name="stat-cell">
                            <xsl:with-param name="stat" select="$current-node"/>
                            <xsl:with-param name="name" select="local-name($current-node)"/>
                        </xsl:call-template>

                        <!-- Next stat (if exists) -->
                        <xsl:if test="$next-node">
                            <xsl:call-template name="stat-cell">
                                <xsl:with-param name="stat" select="$next-node"/>
                                <xsl:with-param name="name" select="local-name($next-node)"/>
                            </xsl:call-template>
                        </xsl:if>

                        <!-- Next next stat (if exists) -->
                        <xsl:if test="$next-next-node">
                            <xsl:call-template name="stat-cell">
                                <xsl:with-param name="stat" select="$next-next-node"/>
                                <xsl:with-param name="name" select="local-name($next-next-node)"/>
                            </xsl:call-template>
                        </xsl:if>
                    </fo:table-row>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
    </xsl:template>
</xsl:stylesheet>