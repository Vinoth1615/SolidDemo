// -----------------------------
// S: Single Responsibility Principle
// -----------------------------
class Invoice {
    private String id;
    private double amount;

    public Invoice(String id, double amount) {
        this.id = id;
        this.amount = amount;
    }

    public String getId() { return id; }
    public double getAmount() { return amount; }
}

// Handles saving invoices (not printing or calculating)
class InvoiceRepository {
    void save(Invoice invoice) {
        System.out.println("Invoice " + invoice.getId() + " saved to database.");
    }
}

// Handles printing invoices
class InvoicePrinter {
    void print(Invoice invoice) {
        System.out.println("Invoice " + invoice.getId() + " printed with amount: " + invoice.getAmount());
    }
}

// -----------------------------
// O: Open/Closed Principle
// -----------------------------
abstract class Discount {
    abstract double applyDiscount(double amount);
}

class NoDiscount extends Discount {
    double applyDiscount(double amount) { return amount; }
}

class SeasonalDiscount extends Discount {
    double applyDiscount(double amount) { return amount * 0.9; } // 10% off
}

class LoyaltyDiscount extends Discount {
    double applyDiscount(double amount) { return amount * 0.8; } // 20% off
}

// -----------------------------
// L: Liskov Substitution Principle
// -----------------------------
abstract class Payment {
    abstract void pay(double amount);
}

class CreditCardPayment extends Payment {
    void pay(double amount) {
        System.out.println("Paid " + amount + " using Credit Card.");
    }
}

class UPIPayment extends Payment {
    void pay(double amount) {
        System.out.println("Paid " + amount + " using UPI.");
    }
}

// -----------------------------
// I: Interface Segregation Principle
// -----------------------------
interface Printable {
    void print();
}

interface EmailSendable {
    void sendEmail();
}

class InvoiceReport implements Printable, EmailSendable {
    public void print() {
        System.out.println("Printing invoice report...");
    }

    public void sendEmail() {
        System.out.println("Sending invoice report via Email...");
    }
}

// -----------------------------
// D: Dependency Inversion Principle
// -----------------------------
interface Notifier {
    void send(String message);
}

class EmailNotifier implements Notifier {
    public void send(String message) {
        System.out.println("Email sent: " + message);
    }
}

class SMSNotifier implements Notifier {
    public void send(String message) {
        System.out.println("SMS sent: " + message);
    }
}

// High-level module depends on abstraction, not concrete notifier
class InvoiceService {
    private Notifier notifier;
    private Discount discount;

    public InvoiceService(Notifier notifier, Discount discount) {
        this.notifier = notifier;
        this.discount = discount;
    }

    public void processInvoice(Invoice invoice, Payment payment) {
        double finalAmount = discount.applyDiscount(invoice.getAmount());
        payment.pay(finalAmount);
        notifier.send("Invoice " + invoice.getId() + " processed. Final Amount: " + finalAmount);
    }
}

// -----------------------------
// MAIN PROGRAM
// -----------------------------
public class SolidDemo {
    public static void main(String[] args) {
        Invoice invoice = new Invoice("INV001", 1000);

        // SRP: Save and Print
        InvoiceRepository repo = new InvoiceRepository();
        InvoicePrinter printer = new InvoicePrinter();
        repo.save(invoice);
        printer.print(invoice);

        // OCP: Apply discount without modifying existing code
        Discount discount = new SeasonalDiscount(); // can switch to LoyaltyDiscount easily

        // LSP: Pay with any payment method
        Payment payment = new CreditCardPayment();

        // DIP: Inject abstraction (Notifier) instead of concrete dependency
        Notifier notifier = new SMSNotifier();

        InvoiceService service = new InvoiceService(notifier, discount);
        service.processInvoice(invoice, payment);

        // ISP: Reports can be printed and emailed
        InvoiceReport report = new InvoiceReport();
        report.print();
        report.sendEmail();
    }
}
