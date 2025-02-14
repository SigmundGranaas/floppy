<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template match="/">
        <fo:root>
            <!-- Define the page layout -->
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
                    <fo:block font-size="10pt" text-align="right" color="#666666">
                        Pokémon Profile - Page <fo:page-number/>
                    </fo:block>
                </fo:static-content>

                <!-- Footer -->
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="8pt" text-align="center" color="#666666">
                        Generated on <xsl:value-of select="format-dateTime(current-dateTime(), '[D01] [MNn] [Y0001]')"/>
                    </fo:block>
                </fo:static-content>

                <!-- Main Content -->
                <fo:flow flow-name="xsl-region-body">
                    <!-- Pokemon Card -->
                    <fo:block-container>
                        <!-- Title -->
                        <fo:block font-size="24pt" font-weight="bold" text-align="center"
                                  padding="1cm" color="#2C3E50">
                            <xsl:value-of select="SinglePokemonDTO/name"/>
                        </fo:block>

                        <!-- Pokemon Info Box -->
                        <fo:block border="2pt solid #BDC3C7" padding="1cm" margin-bottom="1cm">
                            <fo:table width="100%" table-layout="fixed">
                                <fo:table-column column-width="40%"/>
                                <fo:table-column column-width="60%"/>
                                <fo:table-body>
                                    <fo:table-row>
                                        <!-- Image Cell -->
                                        <fo:table-cell display-align="center">
                                            <fo:block text-align="center">
                                                <xsl:if test="SinglePokemonDTO/imageUrl">
                                                    <fo:external-graphic src="url({SinglePokemonDTO/imageUrl})"
                                                                         content-width="scale-to-fit"
                                                                         width="150pt"
                                                                         scaling="uniform"/>
                                                </xsl:if>
                                            </fo:block>
                                        </fo:table-cell>
                                        <!-- Info Cell -->
                                        <fo:table-cell padding-left="1cm">
                                            <fo:block font-size="14pt" padding-bottom="0.5cm">
                                                <fo:inline font-weight="bold">Type: </fo:inline>
                                                <xsl:value-of select="SinglePokemonDTO/type"/>
                                            </fo:block>
                                            <fo:block font-size="14pt">
                                                <fo:inline font-weight="bold">Level: </fo:inline>
                                                <xsl:value-of select="SinglePokemonDTO/level"/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>
                        </fo:block>

                        <!-- Stats Section -->
                        <fo:block margin-top="1cm">
                            <fo:block font-size="18pt" font-weight="bold" color="#2C3E50"
                                      padding-bottom="0.5cm" border-bottom="2pt solid #BDC3C7">
                                Base Stats
                            </fo:block>
                            <fo:table width="100%" table-layout="fixed" margin-top="0.5cm">
                                <fo:table-column column-width="30%"/>
                                <fo:table-column column-width="70%"/>
                                <fo:table-body>
                                    <xsl:for-each select="SinglePokemonDTO/stats/*">
                                        <fo:table-row>
                                            <fo:table-cell padding="0.3cm">
                                                <fo:block font-weight="bold" text-transform="capitalize">
                                                    <xsl:value-of select="name()"/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="0.3cm">
                                                <fo:block>
                                                    <fo:inline-container width="{.}%" height="16pt">
                                                        <fo:block background-color="#3498DB" height="100%"/>
                                                    </fo:inline-container>
                                                    <fo:inline padding-left="0.3cm">
                                                        <xsl:value-of select="."/>
                                                    </fo:inline>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </xsl:for-each>
                                </fo:table-body>
                            </fo:table>
                        </fo:block>

                        <!-- Moves Section -->
                        <fo:block margin-top="1cm">
                            <fo:block font-size="18pt" font-weight="bold" color="#2C3E50"
                                      padding-bottom="0.5cm" border-bottom="2pt solid #BDC3C7">
                                Move Set
                            </fo:block>
                            <fo:table width="100%" table-layout="fixed" margin-top="0.5cm">
                                <fo:table-column column-width="100%"/>
                                <fo:table-body>
                                    <xsl:for-each select="SinglePokemonDTO/moves">
                                        <fo:table-row>
                                            <fo:table-cell padding="0.3cm" background-color="#ECF0F1">
                                                <fo:block font-size="14pt">
                                                    <xsl:value-of select="."/>
                                                </fo:block>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </xsl:for-each>
                                </fo:table-body>
                            </fo:table>
                        </fo:block>
                    </fo:block-container>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>