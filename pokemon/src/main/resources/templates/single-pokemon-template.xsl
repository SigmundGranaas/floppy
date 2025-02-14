<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template match="/*">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="pokemon-profile"
                                       page-height="29.7cm"
                                       page-width="21cm"
                                       margin-top="1cm"
                                       margin-bottom="1cm"
                                       margin-left="2.5cm"
                                       margin-right="2.5cm">
                    <fo:region-body margin-top="2cm" margin-bottom="2cm"/>
                    <fo:region-before extent="2cm"/>
                    <fo:region-after extent="2cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="pokemon-profile">
                <!-- Header -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block font-size="10pt" text-align="right" color="#666666" font-family="Helvetica">
                        Pokémon Profile - Page <fo:page-number/>
                    </fo:block>
                </fo:static-content>

                <!-- Main Content -->
                <fo:flow flow-name="xsl-region-body">
                    <!-- Title -->
                    <fo:block font-size="32pt" font-weight="bold" text-align="center"
                              padding-bottom="1cm" color="#2C3E50" font-family="Helvetica">
                        <xsl:value-of select="name"/>
                    </fo:block>

                    <!-- Pokemon Info Box -->
                    <fo:block border="2pt solid #BDC3C7" padding="1cm" margin-bottom="1.5cm"
                              background-color="#FFFFFF">
                        <fo:table width="100%" table-layout="fixed">
                            <fo:table-column column-width="40%"/>
                            <fo:table-column column-width="60%"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <!-- Image Cell -->
                                    <fo:table-cell display-align="center">
                                        <fo:block text-align="center">
                                            <xsl:if test="imageUrl">
                                                <fo:external-graphic src="url({imageUrl})"
                                                                     content-width="scale-to-fit"
                                                                     width="200pt"
                                                                     scaling="uniform"/>
                                            </xsl:if>
                                        </fo:block>
                                    </fo:table-cell>
                                    <!-- Info Cell -->
                                    <fo:table-cell padding-left="1cm" display-align="center">
                                        <fo:block font-size="16pt" padding-bottom="0.5cm" font-family="Helvetica">
                                            <fo:inline font-weight="bold">Type: </fo:inline>
                                            <xsl:value-of select="type"/>
                                        </fo:block>
                                        <fo:block font-size="16pt" font-family="Helvetica">
                                            <fo:inline font-weight="bold">Level: </fo:inline>
                                            <xsl:value-of select="level"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>

                    <!-- Stats Section -->
                    <fo:block margin-top="1cm">
                        <fo:block font-size="24pt" font-weight="bold" color="#2C3E50"
                                  padding-bottom="0.5cm" border-bottom="2pt solid #BDC3C7"
                                  font-family="Helvetica">
                            Base Stats
                        </fo:block>
                        <fo:table width="100%" table-layout="fixed" margin-top="0.8cm">
                            <fo:table-column column-width="20%"/>
                            <fo:table-column column-width="65%"/>
                            <fo:table-column column-width="15%"/>
                            <fo:table-body>
                                <xsl:for-each select="stats/*">
                                    <fo:table-row height="30pt">
                                        <fo:table-cell display-align="center">
                                            <fo:block font-weight="bold" text-transform="capitalize"
                                                      font-family="Helvetica" font-size="14pt">
                                                <xsl:value-of select="name()"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell display-align="center" padding-left="0.5cm" padding-right="0.5cm">
                                            <fo:block>
                                                <fo:table width="100%" table-layout="fixed">
                                                    <fo:table-column column-width="proportional-column-width({.})"/>
                                                    <fo:table-column column-width="proportional-column-width({150 - .})"/>
                                                    <fo:table-body>
                                                        <fo:table-row>
                                                            <fo:table-cell>
                                                                <fo:block background-color="#3498DB"
                                                                          padding-top="0.4cm"
                                                                          padding-bottom="0.4cm"/>
                                                            </fo:table-cell>
                                                            <fo:table-cell>
                                                                <fo:block background-color="#ECF0F1"
                                                                          padding-top="0.4cm"
                                                                          padding-bottom="0.4cm"/>
                                                            </fo:table-cell>
                                                        </fo:table-row>
                                                    </fo:table-body>
                                                </fo:table>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell display-align="center">
                                            <fo:block text-align="right" font-family="Helvetica" font-size="14pt">
                                                <xsl:value-of select="."/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>

                    <!-- Moves Section -->
                    <fo:block margin-top="2cm" page-break-before="always">
                        <fo:block font-size="24pt" font-weight="bold" color="#2C3E50"
                                  padding-bottom="0.5cm" border-bottom="2pt solid #BDC3C7"
                                  font-family="Helvetica">
                            Move Set
                        </fo:block>
                        <fo:table width="100%" table-layout="fixed" margin-top="0.8cm">
                            <fo:table-column column-width="100%"/>
                            <fo:table-body>
                                <xsl:for-each select="moves">
                                    <fo:table-row>
                                        <fo:table-cell padding="0.6cm" background-color="#ECF0F1"
                                                       margin-bottom="0.3cm">
                                            <fo:block font-size="16pt" font-family="Helvetica">
                                                <xsl:value-of select="."/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </xsl:for-each>
                            </fo:table-body>
                        </fo:table>
                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>