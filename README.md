hibernate-loadroler
===================

This is helper library for customize lazy-load behavior of [Hibernate](https://hibernate.org/).

## Install
Put `hibernate-loadroler-(version).jar` into your classpath.
This library using the Service Provider Interface. 
So if you want to stop using this library, please remove jar file certainly.

## Usage
Register associations you want to load non-lazy.
```java
Session session = sessionFactory.openSession();
RoleHolders.holderForSession(session)
    .addLoadRole("net.bis5.hibernate.loadroler.entity.User", "attrs");
// Search User by Criteria, HQL, SQL, etc
```

When loadroler is available, `FetchMode` will ignore.
The associations will force load which you're registered by `RoleHolder#addLoadRole`.
If you aren't registered association, that are set `null` value.
Yes, **you couldn't use lazy-loading mechanism !**

## Supported Hibernate versions
In master, support latest stable versions (currently, 5.x).
I have the plan for more older version support such as 3.x.

## License
GNU Lesser General Public License, Version 2.1.
See [License.txt](./License.txt).
