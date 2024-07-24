package com.example.conasforapp.Usuarios.Supabase

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest

class SupabaseClient {

    fun getClient(): SupabaseClient {
        return createSupabaseClient(
            supabaseUrl = "https://zopubgpfftyohbnjaemh.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InpvcHViZ3BmZnR5b2hibmphZW1oIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTkyNTE0MDEsImV4cCI6MjAzNDgyNzQwMX0.pUwXvAoo8CbYSZvXMaokbc2hPRX9MFjH19f42BkTcb4"
        ) {
            install(Postgrest)
        }
    }
}