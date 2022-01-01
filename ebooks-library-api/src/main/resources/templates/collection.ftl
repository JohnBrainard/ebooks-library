<#import "layout/main.ftl" as main>
<#import "layout/components.ftl" as components>

<@main.main title="${collection.name}" active="collections">
    <h2>${collection.name}</h2>

    <div id="books">
        <#list collection.entries as entry>
            <@components.collectionEntry entry=entry showAuthors=true />
        </#list>
    </div>

    <div style="padding: 1em;">
        <form method="post" action="/collection/${collection.id}/index">
            <input type="submit" value="Reindex Collection"/>
        </form>
    </div>
</@main.main>
