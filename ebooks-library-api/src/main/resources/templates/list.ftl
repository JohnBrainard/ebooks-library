<#import "layout/main.ftl" as layouts>
<#import "layout/components.ftl" as components>

<@layouts.main title="${list.name}" active="lists">
    <div id="books">
        <div id="books">
            <#list list.entries as entry>
                <@components.collectionEntry entry=entry showCollection=true />
            </#list>
        </div>
    </div>
</@layouts.main>
