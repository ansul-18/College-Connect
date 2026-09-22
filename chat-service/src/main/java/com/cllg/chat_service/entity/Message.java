package com.cllg.chat_service.entity;

import jakarta.persistence.*;

import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(
            name = "conversation_id",
            nullable = false
    )
    private Long conversationId;

    @Column(
            name = "sender_id",
            nullable = false
    )
    private Long senderId;

    @Column(
            name = "sender_role",
            nullable = false,
            length = 30
    )
    private String senderRole;

    /*
     * For normal text messages this contains
     * the actual message.
     *
     * For attachment messages this can contain
     * an optional caption.
     */
    @Column(
            columnDefinition = "TEXT"
    )
    private String content;

    /*
     * TEXT
     * IMAGE
     * DOCUMENT
     */
    @Column(
            name = "message_type",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private String messageType = "TEXT";

    /*
     * URL/path of uploaded file
     */
    @Column(
            name = "file_url",
            length = 1000
    )
    private String fileUrl;

    /*
     * Original file name
     */
    @Column(
            name = "file_name",
            length = 255
    )
    private String fileName;

    /*
     * MIME type
     * image/png
     * application/pdf
     * etc.
     */
    @Column(
            name = "file_type",
            length = 150
    )
    private String fileType;

    /*
     * File size in bytes
     */
    @Column(
            name = "file_size"
    )
    private Long fileSize;

    @Column(
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    @Column(
            name = "is_read",
            nullable = false
    )
    @Builder.Default
    private boolean read = false;

    @PrePersist
    protected void onCreate() {

        this.createdAt =
                LocalDateTime.now();
    }
}