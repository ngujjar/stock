    package com.trade.entity;

    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @Table(name = "users")
    public class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false, unique = true)
        private String username;

        @Column(nullable = false, unique = true)
        private String email;

        @Column(nullable = false)
        private String password;

        private Double balance;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof User)) return false;
            User other = (User) o;
            return id != null && id.equals(other.getId());
        }

        @Override
        public int hashCode() {
            return getClass().hashCode();
        }
    }
