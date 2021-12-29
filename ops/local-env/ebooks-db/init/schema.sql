\c ebooks;

create function text_array_to_string(text[], text, text) returns text as
$$
select array_to_string($1, $2, $3)
$$ language sql cost 1
                immutable;

create schema ebooks;
create extension "uuid-ossp";

create table ebooks.collections
(
    collection_id uuid primary key default uuid_generate_v4(),
    name          text        not null,
    path          text unique not null
);

create table ebooks.books
(
    book_id       uuid primary key default uuid_generate_v4(),
    collection_id uuid   not null references ebooks.collections (collection_id),
    name          text   not null,
    title         text   not null,
    authors       text[] not null  default '{}',
    path          text   not null,
    contents      text[] not null  default '{}',

    constraint unq_ebooks_collection_id_path unique (collection_id, path)
);

create index idx_ts_title on ebooks.books using gin (to_tsvector('english', title));
create index idx_ts_contents on ebooks.books using gin (to_tsvector('english', text_array_to_string(contents, ' ', ' ')));

-- create table ebooks.favorites
-- (
--     book_id uuid references ebooks.books (book_id) primary key
-- );
