<#import "layout/main.ftl" as layouts>
<#import "layout/components.ftl" as components>

<@layouts.main title="Search Books" active="search">
    <h2>Search Results: ${query}</h2>

    <div id="search">
        <form method="get" action="/search">
            <input type="text" name="title"/>
            <input type="submit" value="Search"/>
        </form>
    </div>

    <div id="books">
        <#list results.results as entry>
            <@components.collectionEntry entry=entry showCollection=true />
        </#list>
    </div>
</@layouts.main>
