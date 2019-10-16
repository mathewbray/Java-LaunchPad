########################################################################  Powershell Network Monitor RSS Feed Ticker
#  Created by Brad Voris#  This script is used to generate results for the rssticker.html page
#  Need- run from CFG file, multiple RSS feeds, convert feeds to links
#        remove star from feed########################################################################Script variables
$Dated = (Get-Date -format F)
$RSSFeedstreams = Get-Content "C:\inetpub\wwwroot\cfg\rssfeedstreams.cfg"

#1st RSS Feed Stream
#$WebClient01 = New-Object system.net.webclient
#$RSSFeed01 = [xml]$WebClient01.DownloadString('http://www.travis.af.mil/rss/TopStoriesByTab.asp?tabId=145817')
#$RSSFeedVar01 = $RSSFeed01.rss.channel.item | Select-Object title -First 5 | ConvertTo-Html
#2nd RSS Feed Stream
$WebClient02 = New-Object system.net.webclient
$RSSFeed02 = [xml]$WebClient02.DownloadString('https://news.google.com/news?cf=all&hl=en&pz=1&ned=us&topic=w&output=rss')
$RSSFeedVar02 = $RSSFeed02.rss.channel.item | Select-Object title -First 5 | ConvertTo-Html

#CSS Coding
$CSS = Get-Content "C:\inetpub\wwwroot\css\rss.css "

#HTML Header Coding
$HTMLHead = @"
<!DOCTYPE html>
<HEAD>
<META charset="UTF-8">
<TITLE>PSNetMon - RSS Ticker Module</TITLE>
<CENTER>
<STYLE>BODY{background-color:#bcbcab; }</STYLE></HEAD>
"@

#HTML Body Coding
$HTMLBody = @"<TABLE align="right" border="0" style="margin-top: -2px; margin-bottom: -10px;"><tr><td><TABLE align="right" border="0" style="margin-top: -28px; margin-bottom: -13px;"><TR bgcolor=#bcbcab><!--<TD><font size=2>$RSSFeedVar01</TD>-->
<TD><font size=2 color=#333333>$RSSFeedVar02</TD></TR></TABLE><br><TABLE align="right" border="0" style="margin-top: 0px; margin-bottom: 0px;"><tr><td><I><font size=1>$Dated</font></I></td></tr></table></td></tr></table>"@

#Export to HTML
$Script | ConvertTo-HTML -Head $HTMLHead -Body $HTMLBody | Out-file "C:\inetpub\wwwroot\gen\rssticker.html"

