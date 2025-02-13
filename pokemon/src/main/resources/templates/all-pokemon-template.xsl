<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- Root template to catch document root -->
    <xsl:template match="/">
        <xsl:apply-templates select="*"/>
    </xsl:template>

    <!-- Case-insensitive match for AllPokemonDTO -->
    <xsl:template match="*[translate(local-name(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='allpokemondto']">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="all-pokemon"
                                       page-height="29.7cm"
                                       page-width="21cm"
                                       margin="1.5cm">
                    <fo:region-body margin-top="2cm"/>
                    <fo:region-before extent="2cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="all-pokemon">
                <!-- Header -->
                <fo:static-content flow-name="xsl-region-before">
                    <fo:block font-size="20pt" font-weight="bold" text-align="center">
                        Pokemon Collection
                    </fo:block>
                    <fo:block font-size="10pt" text-align="right">
                        Sorted by: <xsl:value-of select="sortBy"/>
                    </fo:block>
                    <fo:block text-align="center" space-before="0.5cm">
                        <fo:leader leader-pattern="rule" leader-length="100%" rule-thickness="1pt"/>
                    </fo:block>
                </fo:static-content>

                <fo:flow flow-name="xsl-region-body">
                    <!-- Pokemon Table -->
                    <fo:table width="100%" table-layout="fixed" border-collapse="separate" border-separation="0.5cm">
                        <fo:table-column column-width="25%"/>
                        <fo:table-column column-width="15%"/>
                        <fo:table-column column-width="60%"/>

                        <!-- Table Header -->
                        <fo:table-header>
                            <fo:table-row font-weight="bold" background-color="#E8E8E8">
                                <fo:table-cell padding="0.2cm">
                                    <fo:block>Name</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding="0.2cm">
                                    <fo:block>Level</fo:block>
                                </fo:table-cell>
                                <fo:table-cell padding="0.2cm">
                                    <fo:block>Type</fo:block>
                                </fo:table-cell>
                            </fo:table-row>
                        </fo:table-header>

                        <!-- Table Body -->
                        <fo:table-body>
                            <xsl:for-each select="pokemon">
                                <fo:table-row>
                                    <xsl:if test="position() mod 2 = 0">
                                        <xsl:attribute name="background-color">#F8F8F8</xsl:attribute>
                                    </xsl:if>

                                    <fo:table-cell padding="0.2cm" border="0.5pt solid #D0D0D0">
                                        <fo:block>
                                            <xsl:value-of select="name"/>
                                        </fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell padding="0.2cm" border="0.5pt solid #D0D0D0">
                                        <fo:block>
                                            <xsl:value-of select="level"/>
                                        </fo:block>
                                    </fo:table-cell>

                                    <fo:table-cell padding="0.2cm" border="0.5pt solid #D0D0D0">
                                        <fo:block>
                                            <xsl:value-of select="type"/>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </xsl:for-each>
                        </fo:table-body>
                    </fo:table>

                    <!-- Summary Footer -->
                    <fo:block space-before="1cm" font-size="12pt">
                        Total Pokemon: <xsl:value-of select="count(pokemon)"/>
                    </fo:block>

                    <!-- Filter Information -->
                    <xsl:if test="filterBy != ''">
                        <fo:block space-before="0.5cm" font-size="10pt" font-style="italic">
                            Filtered by: <xsl:value-of select="filterBy"/>
                        </fo:block>
                    </xsl:if>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>

</xsl:stylesheet>