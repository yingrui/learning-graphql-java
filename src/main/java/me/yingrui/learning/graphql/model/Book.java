package me.yingrui.learning.graphql.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "books")
public class Book {

    @Id
    private String id;
    @Column(name = "name")
    private String name;
    @Column(name = "page_count")
    private int pageCount;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

}
