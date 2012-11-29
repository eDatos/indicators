[#ftl]
<?xml version="1.0" encoding="utf-8"?>
<feed xmlns="http://www.w3.org/2005/Atom">
	<title>${indicatorsSystemTitle}</title>
	<link href="${feedUrl}" rel="self"/>
	<link href="${siteUrl}"/>
	<id>${siteUrl}</id>
	<updated>${lastUpdated?datetime?iso_utc}</updated>
	<author>
		<name>${authorName}</name>
	</author>

	[#list indicatorsSystemHistories as systemHist]
		<entry>
			<id>${siteUrl}/${systemHist.version}</id>
			<title>${systemUpdateTitleText} ${systemHist.publicationDate?date}</title>
			<link href="${siteUrl}" rel="alternate"/>
			<updated>${systemHist.publicationDate?datetime?iso_utc}</updated>
			<content type="html">${systemUpdateContentText}</content>
			<author>
				<name>${authorName}</name>
			</author>
		</entry>
	[/#list]
</feed>