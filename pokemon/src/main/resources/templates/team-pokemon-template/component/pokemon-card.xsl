<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:import href="../utils/stats.xsl"/>
    <xsl:import href="../style/theme.xsl"/>
    <xsl:template match="team">
        <fo:block-container margin-bottom="1cm"
                            padding="0.8cm"
                            border="2pt solid {$color-secondary}"
                            background-color="#ffffff">
            <xsl:call-template name="pokemon-header"/>
            <xsl:call-template name="pokemon-info"/>
            <xsl:call-template name="pokemon-moves"/>
            <xsl:call-template name="pokemon-stats"/>
        </fo:block-container>
        <fo:block break-before="page"/>
    </xsl:template>

    <xsl:template name="pokemon-header">
        <fo:block-container background-color="{$color-primary}"
                            padding="0.5cm"
                            margin="-0.8cm"
                            margin-bottom="0.5cm">
            <fo:block font-size="24pt"
                      font-weight="bold"
                      color="white">
                <xsl:value-of select="name"/>
            </fo:block>
        </fo:block-container>
    </xsl:template>

    <xsl:template name="pokemon-info">
        <fo:table width="100%" table-layout="fixed">
            <fo:table-column column-width="50%"/>
            <fo:table-column column-width="50%"/>
            <fo:table-body>
                <fo:table-row>
                    <fo:table-cell>
                        <fo:block font-size="16pt" color="{$color-primary}">
                            Type: <xsl:value-of select="type"/>
                        </fo:block>
                    </fo:table-cell>
                    <fo:table-cell>
                        <fo:block font-size="16pt" color="{$color-primary}">
                            Level: <xsl:value-of select="level"/>
                        </fo:block>
                    </fo:table-cell>
                </fo:table-row>
            </fo:table-body>
        </fo:table>
    </xsl:template>

    <xsl:template name="pokemon-moves">
        <fo:block font-size="14pt"
                  font-weight="bold"
                  color="{$color-primary}"
                  space-before="0.8cm"
                  space-after="0.3cm">
            Moves:
        </fo:block>
        <fo:table width="100%" table-layout="fixed">
            <fo:table-column column-width="50%"/>
            <fo:table-column column-width="50%"/>
            <fo:table-body>
                <!-- Process moves in pairs -->
                <xsl:for-each select="moves">
                    <xsl:if test="position() mod 2 = 1">
                        <fo:table-row>
                            <!-- Current move -->
                            <fo:table-cell padding="0.2cm">
                                <fo:block font-size="12pt"
                                          background-color="#edf2f7"
                                          padding="0.3cm">
                                    <xsl:value-of select="."/>
                                </fo:block>
                            </fo:table-cell>

                            <!-- Next move (if exists) -->
                            <xsl:choose>
                                <xsl:when test="following-sibling::moves[1]">
                                    <fo:table-cell padding="0.2cm">
                                        <fo:block font-size="12pt"
                                                  background-color="#edf2f7"
                                                  padding="0.3cm">
                                            <xsl:value-of select="following-sibling::moves[1]"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </xsl:when>
                                <xsl:otherwise>
                                    <fo:table-cell/>
                                </xsl:otherwise>
                            </xsl:choose>
                        </fo:table-row>
                    </xsl:if>
                </xsl:for-each>
            </fo:table-body>
        </fo:table>
    </xsl:template>
</xsl:stylesheet>