<#function getTitle value>
    <#if value == "">
        <#return "eBook Library"/>
    <#else>
        <#return "eBook Library - ${value}" />
    </#if>
</#function>

<#macro main title="">
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
    </div>

    <div id="root">
        <#nested />
    </div>

    </body>
    </html>
</#macro>