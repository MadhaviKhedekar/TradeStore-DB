
package com.example.tradestorage.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Data
@Getter
@Setter
public class Trade {
    @Id
    private String tradeId;
    private int version;
    private String counterPartyId;
    private String bookId;
    private LocalDate maturityDate;
    private LocalDate createdDate;
    private String expired;

    @Override
    public String toString() {
        return "Trade{" +
                "tradeId='" + tradeId + '\'' +
                ", version=" + version +
                ", counterPartyId='" + counterPartyId + '\'' +
                ", bookId='" + bookId + '\'' +
                ", maturityDate=" + maturityDate +
                ", createdDate=" + createdDate +
                ", expired='" + expired + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return version == trade.version && Objects.equals(tradeId, trade.tradeId) && Objects.equals(counterPartyId, trade.counterPartyId) && Objects.equals(bookId, trade.bookId) && Objects.equals(maturityDate, trade.maturityDate) && Objects.equals(createdDate, trade.createdDate) && Objects.equals(expired, trade.expired);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tradeId, version, counterPartyId, bookId, maturityDate, createdDate, expired);
    }
}
