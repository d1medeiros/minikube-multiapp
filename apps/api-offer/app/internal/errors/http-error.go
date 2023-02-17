package errors

import (
	"errors"
	"fmt"
)

var heType *HttpError

var NotFoundError = New(404, "not found")
var InternalError = New(500, "internal error")

type HttpError struct {
	Message    string
	StatusCode int
}

func (e *HttpError) Error() string {
	return e.String()
}

func (h *HttpError) String() string {
	return fmt.Sprintf("%d %s", h.StatusCode, h.Message)
}

func New(code int, msg string) error {
	return &HttpError{
		Message:    msg,
		StatusCode: code,
	}
}

func IsHttpError(err error) bool {
	return errors.As(err, &heType)
}

func As(err error, i any) bool {
	return errors.As(err, i)
}

func NewDefault(msg string) error {
	return errors.New(msg)
}
