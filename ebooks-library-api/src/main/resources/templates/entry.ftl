<#import "layout/main.ftl" as main>
<#import "layout/components.ftl" as components>

<@main.main title="${entry.title}" active="collections">
    <h2>${entry.title}</h2>

    <div id="book">
        <div>Authors: <#list entry.authors as author>${author}</#list></div>
        <div>Pages: ${entry.pageCount}</div>
        <div>Path: <a href="${entry.downloadUrl}" target="_blank">${entry.path}</a></div>
        <div>Collection: <a href="/collection/${entry.collectionId}">${entry.collectionName}</a></div>

        <h3>Contents</h3>
        <ul>
            <#list entry.contents as contentsItem>
                <li>${contentsItem}</li>
            </#list>
        </ul>

        <h3><a href="${entry.downloadUrl}" target="_blank">${entry.path}</a></h3>

        <h2>Lists</h2>
        <ul>
            <#list listsContainingBook as list>
                <li>${list.name}</li>
            </#list>
        </ul>
    </div>
</@main.main>
