CREATE TABLE links (
    id MEDIUMINT NOT NULL AUTO_INCREMENT,
    subject varchar(512) NOT NULL,
    subjectType varchar(512) NOT NULL,
    
    predicate varchar(128) NOT NULL,
    predicateType varchar(128) NOT NULL,

    object varchar(512) NOT NULL,
    objectType varchar(512) NOT NULL,
    
    provenance varchar(512) NOT NULL,

    PRIMARY KEY (id)
);

CREATE INDEX subject ON links (subject);
CREATE INDEX subjectType ON links (subjectType);
CREATE INDEX predicate ON links (predicate);
CREATE INDEX object ON links (object);
CREATE INDEX objectType ON links (objectType);
CREATE INDEX provenance ON links (provenance);
CREATE UNIQUE INDEX spop ON links (subject, subjectType, predicate, predicateType, object, objectType, provenance);