<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1" xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:template match="/">
        <xsl:apply-templates select="*"/>
    </xsl:template>
    <xsl:template match="*[translate(local-name(), 'ABCDEFGHIJKLMNOPQRSTUVWXYZ', 'abcdefghijklmnopqrstuvwxyz')='singlepokemondto']">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="pokemon"
                                       page-height="29.7cm"
                                       page-width="21cm"
                                       margin="2cm">
                    <fo:region-body/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <fo:page-sequence master-reference="pokemon">
                <fo:flow flow-name="xsl-region-body">
                    <fo:block font-size="24pt" font-weight="bold" space-after="1cm">
                        <xsl:value-of select="name"/>
                    </fo:block>

                    <fo:block font-size="14pt" space-after="0.5cm">
                        Level: <xsl:value-of select="level"/>
                    </fo:block>

                    <fo:block font-size="14pt" space-after="0.5cm">
                        Type: <xsl:value-of select="type"/>
                    </fo:block>

                    <fo:block font-size="16pt" font-weight="bold" space-after="0.5cm">
                        Moves:
                    </fo:block>
                    <xsl:for-each select="moves">
                        <fo:block font-size="12pt" margin-left="1cm">
                            • <xsl:value-of select="."/>
                        </fo:block>
                    </xsl:for-each>

                    <fo:block font-size="16pt" font-weight="bold" space-before="1cm" space-after="0.5cm">
                        Stats:
                    </fo:block>
                    <xsl:for-each select="stats/*">
                        <fo:block font-size="12pt" margin-left="1cm">
                            • <xsl:value-of select="name()"/>: <xsl:value-of select="."/>
                        </fo:block>
                    </xsl:for-each>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>