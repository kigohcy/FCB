<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	
	<xsl:output method="xml" encoding="UTF-8"/>
	
	<!-- *********************************** XML Header Include *************************************************-->	
	<xsl:include href="xsl/common/xml_header_r.xsl"/>
	
	<xsl:template match="XML">
		<xsl:element name="ROOT">
			<xsl:element name="Element">
				<xsl:attribute name="class"><xsl:value-of select="'com.hitrust.bank.telegram.res.FCB91970266ResponseInfo'"/></xsl:attribute>
				<!-- Header -->
				<xsl:call-template name="XML_Header"/>
				<!-- Body -->
				
				<xsl:element name="Array">
					<xsl:attribute name="name"><xsl:value-of select="'records'" /></xsl:attribute>
					<xsl:attribute name="class"><xsl:value-of select="'[Lcom.hitrust.bank.telegram.res.FCB91970266Record;'" /></xsl:attribute>
					<xsl:for-each select="TxRs/Record">
						<xsl:element name="Element">
							<xsl:attribute name="class"><xsl:value-of select="'com.hitrust.bank.telegram.res.FCB91970266Record'" /></xsl:attribute>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'OBUCD'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "OBUCD" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'custID'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "CustID" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'repeatSeq'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "RepeatSeq" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'custName'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "CustName" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'birthday'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "Birthday" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'bossName'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "BossName" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'bossBirth'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "BossBirth" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'bossID'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "BossID" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'nationality'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "Nationality" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'bizType'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "BizType" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'bizType2'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "BizType2" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'mainAplyBnk'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "MainAplyBnk" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'homeTel'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "HomeTel" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'comTel'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "ComTel" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'mobile'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "Mobile" /></xsl:attribute>
							</xsl:element>
							<xsl:element name="Column">
								<xsl:attribute name="name"> <xsl:value-of select="'fax'"/></xsl:attribute>
								<xsl:attribute name="value"><xsl:value-of select= "Fax" /></xsl:attribute>
							</xsl:element>
						</xsl:element>
					</xsl:for-each>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'addressPostCode'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/AddressPostCode"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'address'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/Address"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'comAddressPostCode'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/ComAddressPostCode"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'comAddress'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/ComAddress"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'custBiz'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/CustBiz"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'custSales'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/CustSales"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'custType'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/CustType"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'sex'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/Sex"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'eduRecord'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/EduRecord"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'marriageStatus'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/MarriageStatus"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'OBUID'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/OBUID"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'comType'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/ComType"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'riskFlag'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/RiskFlag"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'warningFlag'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/WarningFlag"/></xsl:attribute>
				</xsl:element>
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'riskScore'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/RiskScore"/></xsl:attribute>
				</xsl:element>
				
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'riskLevel'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/RiskLevel"/></xsl:attribute>
				</xsl:element>
				
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'riskChangeDate'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/RiskChangeDate"/></xsl:attribute>
				</xsl:element>
				
				<xsl:element name="Column">
					<xsl:attribute name="name"> <xsl:value-of select="'twoFATelNo'"/></xsl:attribute>
					<xsl:attribute name="value"><xsl:value-of select="TxRs/2FATelNo"/></xsl:attribute>
				</xsl:element>
			</xsl:element>								
		</xsl:element>
	</xsl:template>
</xsl:stylesheet>