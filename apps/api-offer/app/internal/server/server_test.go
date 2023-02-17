package server

import (
	"fmt"
	"github.com/gofiber/fiber/v2"
	"net/http"
	"net/http/httptest"
	"testing"
)

func Test_Get(t *testing.T) {
	app := fiber.New()
	app.Get("/", GetOffer)
	// http.Request
	req := httptest.NewRequest("GET", "http://google.com", nil)
	req.Form.Set("account_id", "123")

}

func TestGetOffer(t *testing.T) {
	tests := []struct {
		name    string
		req     *http.Request
		mockFn  func(req *http.Request)
		wantErr bool
		want    int
	}{
		{
			name: "name",
			req:  httptest.NewRequest("GET", "http://google.com", nil),
			mockFn: func(req *http.Request) {
				req.URL.Query().Set("account_id", "123")
				req.URL.Query().Add("account_id", "123")
			},
			wantErr: false,
			want:    200,
		},
	}
	for _, tt := range tests {
		t.Run(tt.name, func(t *testing.T) {
			app := fiber.New()
			tt.mockFn(tt.req)
			tt.req.URL.Query().Set("account_id", "123")
			tt.req.URL.Query().Add("account_id", "123")
			app.Get("/", GetOffer)
			res, err := app.Test(tt.req)
			if err != nil {
				t.Error(err)
			}
			fmt.Println(res.StatusCode)
		})
	}
}
