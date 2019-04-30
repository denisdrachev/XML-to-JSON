package pack.jaxb;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;
import java.io.Serializable;
import java.util.List;


@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Envelope")
public class Envelope{

    @XmlElement(name = "Body")
    private Body Body;


    public Body getBody() {
        return Body;
    }

    public void setBody(Body Body) {
        this.Body = Body;
    }

    @Override
    public String toString() {
        return "Envelope{" +
                "Body=" + Body +
                '}';
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Body
{
    @XmlElement(namespace = "wsapi:Payment")
    private InnerClass sendPayment;


    public InnerClass getSendPayment() {
        return sendPayment;
    }

    public void setSendPayment(InnerClass sendPayment) {
        this.sendPayment = sendPayment;
    }

    @Override
    public String toString() {
        return "Body{" +
                "sendPayment=" + sendPayment.toString() +
                '}';
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class InnerClass implements Serializable
{
    @XmlElement(name = "token")
    private String token;

    @XmlElement(name="cardNumber")
    private String cardNumber;

    @XmlElement(name="requestId")
    private String requestId;

    @XmlElement(name="amount")
    private String amount;

    @XmlElement(name="currency")
    private String currency;

    @XmlElement(name="account", namespace = "wsapi:Utils")
    private List<Account> account;

    @XmlElement(name="page")
    private String page;

    private List<Field> field;

    public InnerClass() {
    }


    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public List<Account> getAccount() {
        return account;
    }

    public void setAccount(List<Account> account) {
        this.account = account;
    }

    public List<Field> getField() {
        return field;
    }

    public void setField(List<Field> field) {
        this.field = field;
    }

    @Override
    public String toString() {
        return "InnerClass{" +
                "token='" + token + '\'' +
                ", cardNumber='" + cardNumber + '\'' +
                ", requestId='" + requestId + '\'' +
                ", amount='" + amount + '\'' +
                ", currency='" + currency + '\'' +
                ", account=" + account +
                ", page='" + page + '\'' +
                ", field=" + field +
                '}';
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Field implements Serializable {

    @XmlAttribute
    private String id;

    @XmlAttribute
    private String value;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Field{" +
                "id='" + id + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

@XmlAccessorType(XmlAccessType.FIELD)
class Account{

    @XmlAttribute(name = "type")
    private String type;

    @XmlValue
    private String value;

    public Account() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public String toString() {
        return "Account{" +
                "type='" + type + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}

