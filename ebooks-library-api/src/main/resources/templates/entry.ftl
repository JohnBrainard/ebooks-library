<#ftl output_format="HTML">

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
        <ul class="contents">
            <#list entry.contents as contentsItem>
                <li class="level_${contentsItem.level}">
                    <a href="${contentsItem.url?no_esc}" target="_blank">${contentsItem.title}</a>
                </li>
            </#list>
        </ul>

        <h3><a href="${entry.downloadUrl}" target="_blank">${entry.path}</a></h3>

        <h2>Lists</h2>
        <#list listsContainingBook>
            <h3>This book is in the following list(s)...</h3>
            <ul>
                <#items as list>
                    <li>
                        <a href="/lists/${list.id}">${list.name}</a>
                    </li>
                </#items>
            </ul>
        <#else>
            <h3>This book isn't in any lists yet.</h3>
        </#list>

        <#list lists>
            <h3>Add to list...</h3>
            <form method="post" action="/collection/${entry.collectionId}/entries/${entry.id}/lists">
                <select name="list">
                    <#items  as list>
                        <option value="${list.id}">${list.name}</option>
                    </#items>
                </select>
                <input type="submit" value="Add to list">
            </form>
        </#list>
    </div>
</@main.main>
