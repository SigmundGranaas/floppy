<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.1"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">
    <xsl:import href="../style/theme.xsl"/>
    <xsl:template name="team-header">
        <xsl:param name = "teamName" />
        <xsl:param name = "trainerName" />
        <fo:block-container background-color="{$color-background}"
                            padding="1cm"
                            margin-bottom="1cm">
            <fo:block font-size="32pt"
                      font-weight="bold"
                      text-align="center"
                      color="{$color-primary}">
                <xsl:value-of select="$teamName"/>
            </fo:block>

            <fo:block font-size="18pt"
                      text-align="center"
                      color="#34495e"
                      space-before="0.5cm">
                Trainer: <xsl:value-of select="$trainerName"/>
            </fo:block>
        </fo:block-container>
    </xsl:template>
</xsl:stylesheet>