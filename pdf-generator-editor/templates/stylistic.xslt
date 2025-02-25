<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- Main template -->
    <xsl:template match="/">
        <fo:root>
            <!-- Define the page layout -->
            <fo:layout-master-set>
                <fo:simple-page-master master-name="A4"
                                       page-width="210mm"
                                       page-height="297mm"
                                       margin-top="2cm"
                                       margin-bottom="2cm"
                                       margin-left="3cm"
                                       margin-right="3cm">
                    <fo:region-body margin-top="0cm"/>
                </fo:simple-page-master>
            </fo:layout-master-set>

            <!-- Page content -->
            <fo:page-sequence master-reference="A4">
                <fo:flow flow-name="xsl-region-body">

                    <!-- Single column layout -->
                    <fo:block font-family="Helvetica, Arial, sans-serif" line-height="1.5">

                        <!-- Name - large, bold, all caps -->
                        <fo:block font-size="36pt"
                                  font-weight="bold"
                                  font-family="Helvetica, Arial, sans-serif"
                                  color="#000000"
                                  letter-spacing="3pt"
                                  text-transform="uppercase"
                                  space-after="0.2cm"
                                  padding-bottom="0.2cm">
                            <xsl:value-of select="//personalInfo/name"/>
                        </fo:block>

                        <!-- Title - elegant, understated -->
                        <fo:block font-size="14pt"
                                  letter-spacing="1pt"
                                  color="#333333"
                                  font-weight="normal"
                                  text-transform="uppercase"
                                  space-after="0.5cm">
                            <xsl:value-of select="//personalInfo/title"/>
                        </fo:block>

                        <!-- Contact information in a single line -->
                        <fo:block font-size="9pt"
                                  letter-spacing="0.5pt"
                                  space-after="1.5cm"
                                  color="#666666">
                            <xsl:value-of select="//personalInfo/contact/email"/> •
                            <xsl:value-of select="//personalInfo/contact/phone"/> •
                            <xsl:value-of select="//personalInfo/contact/location"/>
                        </fo:block>

                        <!-- Profile section -->
                        <fo:block space-after="1.5cm">
                            <fo:block font-size="12pt"
                                      font-weight="bold"
                                      text-transform="uppercase"

                                      color="#000000"
                                      space-after="0.5cm"
                                      border-bottom="0.5pt solid #cccccc"
                                      padding-bottom="0.2cm">
                                Profile
                            </fo:block>

                            <fo:block font-size="10pt"
                                      text-align="justify"
                                      space-after="0.4cm"
                                      line-height="1.6">
                                <xsl:value-of select="//profile"/>
                            </fo:block>
                        </fo:block>

                        <!-- Experience section -->
                        <fo:block space-after="1.5cm">
                            <fo:block font-size="12pt"
                                      font-weight="bold"
                                      text-transform="uppercase"

                                      color="#000000"
                                      space-after="0.5cm"
                                      border-bottom="0.5pt solid #cccccc"
                                      padding-bottom="0.2cm">
                                Experience
                            </fo:block>

                            <xsl:for-each select="//experience">
                                <fo:block margin-top="0.7cm" margin-bottom="0.9cm">
                                    <!-- Position and Company on the same line with spacing between -->
                                    <fo:block font-size="11pt"
                                              font-weight="bold"
                                              space-after="0.1cm">
                                        <xsl:value-of select="position"/>
                                        <fo:inline font-weight="normal" color="#666666"> — <xsl:value-of select="company"/></fo:inline>
                                    </fo:block>

                                    <fo:block font-size="9pt"
                                              font-style="italic"
                                              color="#666666"
                                              space-after="0.3cm">
                                        <xsl:value-of select="period"/>
                                    </fo:block>

                                    <fo:block font-size="10pt"
                                              text-align="justify"
                                              space-after="0.3cm"
                                              line-height="1.5">
                                        <xsl:value-of select="description"/>
                                    </fo:block>

                                    <fo:list-block provisional-distance-between-starts="5mm">
                                        <xsl:for-each select="achievements/node()">
                                            <fo:list-item space-after="0.2cm">
                                                <fo:list-item-label end-indent="label-end()">
                                                    <fo:block>—</fo:block>
                                                </fo:list-item-label>
                                                <fo:list-item-body start-indent="body-start()">
                                                    <fo:block font-size="9.5pt" line-height="1.4"><xsl:value-of select="."/></fo:block>
                                                </fo:list-item-body>
                                            </fo:list-item>
                                        </xsl:for-each>
                                    </fo:list-block>
                                </fo:block>
                            </xsl:for-each>
                        </fo:block>

                        <!-- Education section -->
                        <fo:block space-after="1.5cm">
                            <fo:block font-size="12pt"
                                      font-weight="bold"
                                      text-transform="uppercase"

                                      color="#000000"
                                      space-after="0.5cm"
                                      border-bottom="0.5pt solid #cccccc"
                                      padding-bottom="0.2cm">
                                Education
                            </fo:block>

                            <xsl:for-each select="//education">
                                <fo:block margin-top="0.4cm" margin-bottom="0.4cm">
                                    <fo:block font-size="11pt"
                                              font-weight="bold"
                                              space-after="0.1cm">
                                        <xsl:value-of select="degree"/>
                                    </fo:block>

                                    <fo:block font-size="10pt"
                                              space-after="0.1cm">
                                        <xsl:value-of select="institution"/>
                                        <fo:inline font-style="italic" color="#666666"> • <xsl:value-of select="year"/></fo:inline>
                                    </fo:block>
                                </fo:block>
                            </xsl:for-each>
                        </fo:block>

                        <!-- Skills section in a clean format -->
                        <fo:block space-after="1.5cm">
                            <fo:block font-size="12pt"
                                      font-weight="bold"
                                      text-transform="uppercase"

                                      color="#000000"
                                      space-after="0.5cm"
                                      border-bottom="0.5pt solid #cccccc"
                                      padding-bottom="0.2cm">
                                Skills
                            </fo:block>

                            <xsl:for-each select="//skills">
                                <fo:block margin-top="0.4cm" margin-bottom="0.5cm">
                                    <fo:block font-size="10pt"
                                              font-weight="bold"
                                              space-after="0.2cm">
                                        <xsl:value-of select="category"/>
                                    </fo:block>

                                    <fo:block font-size="10pt"
                                              space-after="0.3cm">
                                        <xsl:for-each select="items/node()">
                                            <xsl:value-of select="."/>
                                            <xsl:if test="position() != last()"> • </xsl:if>
                                        </xsl:for-each>
                                    </fo:block>
                                </fo:block>
                            </xsl:for-each>
                        </fo:block>

                        <!-- Languages and Certifications in a single row -->
                        <fo:table width="100%" table-layout="fixed">
                            <fo:table-column column-width="proportional-column-width(1)"/>
                            <fo:table-column column-width="proportional-column-width(1)"/>
                            <fo:table-body>
                                <fo:table-row>
                                    <!-- Languages -->
                                    <fo:table-cell>
                                        <fo:block space-after="0.8cm">
                                            <fo:block font-size="12pt"
                                                      font-weight="bold"
                                                      text-transform="uppercase"

                                                      color="#000000"
                                                      space-after="0.5cm"
                                                      border-bottom="0.5pt solid #cccccc"
                                                      padding-bottom="0.2cm">
                                                Languages
                                            </fo:block>

                                            <xsl:for-each select="//languages">
                                                <fo:block font-size="10pt" space-after="0.2cm" margin-top="0.4cm">
                                                    <fo:inline font-weight="bold"><xsl:value-of select="name"/></fo:inline> — <xsl:value-of select="proficiency"/>
                                                </fo:block>
                                            </xsl:for-each>
                                        </fo:block>
                                    </fo:table-cell>

                                    <!-- Certifications -->
                                    <fo:table-cell padding-left="1cm">
                                        <fo:block space-after="0.8cm">
                                            <fo:block font-size="12pt"
                                                      font-weight="bold"
                                                      text-transform="uppercase"

                                                      color="#000000"
                                                      space-after="0.5cm"
                                                      border-bottom="0.5pt solid #cccccc"
                                                      padding-bottom="0.2cm">
                                                Certifications
                                            </fo:block>

                                            <xsl:for-each select="//certifications">
                                                <fo:block font-size="10pt" space-after="0.3cm" margin-top="0.4cm">
                                                    <fo:inline font-weight="bold"><xsl:value-of select="name"/></fo:inline>
                                                    <fo:block><xsl:value-of select="issuer"/> • <xsl:value-of select="year"/></fo:block>
                                                </fo:block>
                                            </xsl:for-each>
                                        </fo:block>
                                    </fo:table-cell>
                                </fo:table-row>
                            </fo:table-body>
                        </fo:table>

                    </fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>