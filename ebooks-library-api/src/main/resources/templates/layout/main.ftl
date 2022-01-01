<#function getTitle value>
    <#if value == "">
        <#return "eBook Library"/>
    <#else>
        <#return "eBook Library - ${value}" />
    </#if>
</#function>

<#function getActiveStatus(navId, active)>
    <#if navId == active>
        <#return "active" />
    <#else>
        <#return "" />
    </#if>
</#function>

<#macro main title="" active="collections">
    <!DOCTYPE html>
    <html lang="en">
    <head>
        <meta charset="UTF-8">
        <title>${getTitle(title)}</title>
        <link rel="stylesheet" href="/style.css">
    </head>
    <body>

    <div id="header">
        <h1>
            <a href="/" style="text-decoration: none"><i class="icon large">&#128218;</i></a>
            ${getTitle(title)}
        </h1>

        <div class="nav">
            <ul class="nav">
                <li class="${getActiveStatus("collections", active)}">
                    <a href="/">Collections</a>
                </li>
                <li class="${getActiveStatus("lists", active)}">
                    <a href="/lists">Lists</a>
                </li>
            </ul>
        </div>
    </div>

    <div id="root">
        <#nested />
    </div>

    </body>
    </html>
</#macro>