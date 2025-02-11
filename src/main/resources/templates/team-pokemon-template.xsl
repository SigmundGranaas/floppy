<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <xsl:template match="/">
        <xsl:apply-templates select="*"/>
    </xsl:template>

    <xsl:template match="*[translate(local-name(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='teampokemondto']">
        <fo:root>
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

            <fo:page-sequence master-reference="team">
                <!-- Header -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block font-size="10pt" text-align="right" color="#666666">
                        <xsl:value-of select="teamName"/> - Page <fo:page-number/>
                    </fo:block>
                </fo:static-content>

                <!-- Footer -->
                <fo:static-content flow-name="xsl-region-after">
                    <fo:block font-size="8pt" text-align="center" color="#666666">
                        Formation Date: <xsl:value-of select="formationDate"/>
                    </fo:block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body">
                    <!-- Team Header -->
                    <fo:block-container background-color="#f8f9fa" padding="1cm" margin-bottom="1cm">
                        <fo:block font-size="32pt"
                                  font-weight="bold"
                                  text-align="center"
                                  color="#2c3e50">
                            <xsl:value-of select="teamName"/>
                        </fo:block>

                        <fo:block font-size="18pt"
                                  text-align="center"
                                  color="#34495e"
                                  space-before="0.5cm">
                            Trainer: <xsl:value-of select="trainerName"/>
                        </fo:block>
                    </fo:block-container>

                    <!-- Pokemon Grid -->
                    <xsl:for-each select="team">
                        <!-- Force new page after every 2 Pokemon -->
                        <xsl:if test="position() != 1 and position() mod 2 = 1">
                            <fo:block break-before="page"/>
                        </xsl:if>

                        <fo:block-container margin-bottom="1cm"
                                            padding="0.8cm"
                                            border="2pt solid #3498db"
                                            background-color="#ffffff">
                            <!-- Pokemon Header -->
                            <fo:block-container background-color="#3498db"
                                                padding="0.5cm"
                                                margin="-0.8cm"
                                                margin-bottom="0.5cm"
                                               >
                                <fo:block font-size="24pt"
                                          font-weight="bold"
                                          color="white">
                                    <xsl:value-of select="name"/>
                                </fo:block>
                            </fo:block-container>

                            <!-- Type and Level -->
                            <fo:table width="100%" table-layout="fixed">
                                <fo:table-column column-width="50%"/>
                                <fo:table-column column-width="50%"/>
                                <fo:table-body>
                                    <fo:table-row>
                                        <fo:table-cell>
                                            <fo:block font-size="16pt" color="#2c3e50">
                                                Type: <xsl:value-of select="type"/>
                                            </fo:block>
                                        </fo:table-cell>
                                        <fo:table-cell>
                                            <fo:block font-size="16pt" color="#2c3e50">
                                                Level: <xsl:value-of select="level"/>
                                            </fo:block>
                                        </fo:table-cell>
                                    </fo:table-row>
                                </fo:table-body>
                            </fo:table>

                            <!-- Moves -->
                            <fo:block font-size="14pt"
                                      font-weight="bold"
                                      color="#2c3e50"
                                      space-before="0.8cm"
                                      space-after="0.3cm">
                                Moves:
                            </fo:block>
                            <fo:table width="100%" table-layout="fixed">
                                <fo:table-column column-width="proportional-column-width(1)"
                                                 number-columns-repeated="2"/>
                                <fo:table-body>
                                    <xsl:for-each select="moves[position() mod 2 = 1]">
                                        <fo:table-row>
                                            <fo:table-cell padding="0.2cm">
                                                <fo:block font-size="12pt"
                                                          background-color="#edf2f7"
                                                          padding="0.3cm"
                                                        >
                                                    • <xsl:value-of select="."/>
                                                </fo:block>
                                            </fo:table-cell>
                                            <fo:table-cell padding="0.2cm">
                                                <xsl:if test="following-sibling::moves[1]">
                                                    <fo:block font-size="12pt"
                                                              background-color="#edf2f7"
                                                              padding="0.3cm"
                                                             >
                                                        • <xsl:value-of select="following-sibling::moves[1]"/>
                                                    </fo:block>
                                                </xsl:if>
                                            </fo:table-cell>
                                        </fo:table-row>
                                    </xsl:for-each>
                                </fo:table-body>
                            </fo:table>

                            <!-- Stats -->
                            <fo:block font-size="14pt"
                                      font-weight="bold"
                                      color="#2c3e50"
                                      space-before="0.8cm"
                                      space-after="0.3cm">
                                Stats:
                            </fo:block>
                            <fo:table width="100%" table-layout="fixed">
                                <fo:table-column column-width="proportional-column-width(1)"
                                                 number-columns-repeated="3"/>
                                <fo:table-body>
                                    <xsl:for-each select="stats/*[position() mod 3 = 1]">
                                        <fo:table-row>
                                            <xsl:call-template name="stat-cell">
                                                <xsl:with-param name="stat" select="."/>
                                                <xsl:with-param name="name" select="name()"/>
                                            </xsl:call-template>

                                            <xsl:if test="following-sibling::*[1]">
                                                <xsl:call-template name="stat-cell">
                                                    <xsl:with-param name="stat" select="following-sibling::*[1]"/>
                                                    <xsl:with-param name="name" select="following-sibling::*[1]"/>
                                                </xsl:call-template>
                                            </xsl:if>

                                            <xsl:if test="following-sibling::*[2]">
                                                <xsl:call-template name="stat-cell">
                                                    <xsl:with-param name="stat" select="following-sibling::*[2]"/>
                                                    <xsl:with-param name="name" select="following-sibling::*[2]"/>
                                                </xsl:call-template>
                                            </xsl:if>
                                        </fo:table-row>
                                    </xsl:for-each>
                                </fo:table-body>
                            </fo:table>
                        </fo:block-container>
                    </xsl:for-each>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

    <!-- Template for stat cells -->
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

    <xsl:template match="*">
        <xsl:message>
            Warning: Unmatched element: <xsl:value-of select="name()"/>
        </xsl:message>
    </xsl:template>
</xsl:stylesheet>