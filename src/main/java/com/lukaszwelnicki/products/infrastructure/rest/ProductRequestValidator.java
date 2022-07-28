package com.lukaszwelnicki.products.infrastructure.rest;

import com.lukaszwelnicki.products.domain.model.AmountRange;
import com.lukaszwelnicki.products.domain.model.Fee;
import com.lukaszwelnicki.products.domain.model.ProductId;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Try;
import io.vavr.control.Validation;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.lukaszwelnicki.products.domain.model.ProductCommand.CreateProductCommand;
import static com.lukaszwelnicki.products.domain.model.ProductCommand.UpdateProductCommand;
import static io.vavr.control.Validation.*;

class ProductRequestValidator {

    private static final Pattern amountWithMaxTwoDecimals = Pattern.compile("^-?[0-9]+(\\.[0-9]{1,2})?$");

    static Validation<Seq<String>, CreateProductCommand> validate(ProductRequest request) {
        return Validation.combine(
                        valid(request.name()),
                        validateCurrency(request.currency()),
                        validateAmountRange(request.lowerAmount(), request.upperAmount()),
                        validateFees(request.fees()))
                .ap(CreateProductCommand::new);
    }

    static Validation<Seq<String>, UpdateProductCommand> validate(String id, ProductRequest request) {
        return Validation.combine(
                        validateProductId(id),
                        valid(request.name()),
                        validateCurrency(request.currency()),
                        validateAmountRange(request.lowerAmount(), request.upperAmount()),
                        validateFees(request.fees()))
                .ap(UpdateProductCommand::new);
    }

    static Validation<String, ProductId> validateProductId(String uuid) {
        return Try.of(() -> UUID.fromString(uuid))
                .map(ProductId::new)
                .toValidation("Invalid UUID: %s".formatted(uuid));
    }

    private static Validation<String, Currency> validateCurrency(String currency) {
        return Try.of(() -> Currency.getInstance(currency))
                .toValidation("Invalid currency: %s".formatted(currency));
    }

    private static Validation<String, AmountRange> validateAmountRange(String lowerBound, String upperBound) {
        return combine(validateBigDecimal(lowerBound), validateBigDecimal(upperBound))
                .ap(AmountRange::of)
                .getOrElseGet(Validation::invalid)
                .mapError(errors -> errors.mkString(", "));
    }

    private static Validation<String, BigDecimal> validateBigDecimal(String number) {
        return notValidBigDecimal(number) ? invalid("Invalid number format: " + number) :
                valid(new BigDecimal(number));
    }

    private static Validation<String, List<Fee>> validateFees(List<FeeRequest> fees) {
        return Validation.sequence(fees.map(ProductRequestValidator::validateFeeRequest))
                .mapError(errors -> "Invalid fees provided: %s".formatted(errors.mkString(", ")))
                .map(Seq::toList);
    }

    private static Validation<Seq<String>, Fee> validateFeeRequest(FeeRequest feeRequest) {
        return Validation.combine(
                        valid(feeRequest.description()),
                        validateBigDecimal(feeRequest.amount()))
                .ap(Fee::of)
                .getOrElseGet(Validation::invalid);
    }

    private static boolean notValidBigDecimal(String amount) {
        return StringUtils.isEmpty(amount) || !amountWithMaxTwoDecimals.matcher(amount).matches();
    }


}
